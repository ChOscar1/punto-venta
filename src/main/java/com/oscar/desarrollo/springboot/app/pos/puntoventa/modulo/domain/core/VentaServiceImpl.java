package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.core;

import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.entity.*;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.mapper.PedidoMapper;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.mapper.VentaMapper;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.*;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.incoming.VentaService;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.incoming.error.exception.BussinessException;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.incoming.error.exception.ResourceNotFoundException;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.infraestructure.DetalleVentaRepository;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.infraestructure.PedidoRepository;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.infraestructure.ProductoRepository;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.infraestructure.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class VentaServiceImpl implements VentaService {

    @Autowired
    VentaRepository ventaRepository;
    @Autowired
    DetalleVentaRepository detalleRepository;
    @Autowired
    ProductoRepository prodRepository;
    @Autowired
    VentaMapper ventaMapper;
    @Autowired
    PedidoRepository pedidoRepository;
    @Autowired
    PedidoMapper pedidoMapper;

    @Override
    @Transactional
    public PedidoModelResponse registrarVenta(VentaModelRequest request) {

        Pedido pedido = new Pedido();

        pedido.setNombreCliente(request.getNombreCliente());

        pedido.setFecha(LocalDateTime.now(ZoneId.of("America/Mexico_City")));

        pedido.setEstado("PENDIENTE");

        Map<Long, List<DetalleVenta>> detallesPorVendedor = agruparDetallesPorVendedor(request);

        int totalPedido = calcularTotalPedido(request, detallesPorVendedor);

        configurarPago(pedido, request, totalPedido);

        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        crearVentas(request, detallesPorVendedor, pedidoGuardado);

        //pedidoRepository.save(pedidoGuardado);

        return pedidoMapper.responseModel(pedidoGuardado);
    }

    private void configurarPago(Pedido pedido, VentaModelRequest request, int totalPedido) {

        String estadoPago = request.getEstadoPago();

        if (estadoPago == null || estadoPago.isBlank()) {
            throw new BussinessException("Debes indicar el estado del pago");
        }

        Integer montoPagado = request.getMontoPagado() != null ? request.getMontoPagado() : 0;

        if (montoPagado < 0) {
            throw new BussinessException("El monto pagado no puede ser negativo");
        }

        switch (estadoPago) {

            case "SIN_PAGAR":

                if (montoPagado != 0) {
                    throw new BussinessException("Un pedido sin pagar debe tener monto pagado en 0");
                }
                pedido.setMontoPagado(0);
                pedido.setEstadoPago("SIN_PAGAR");
                break;

            case "ANTICIPO":

                if (montoPagado <= 0) {
                    throw new BussinessException("El anticipo debe ser mayor a 0");
                }

                if (montoPagado >= totalPedido) {
                    throw new BussinessException("El anticipo debe ser menor al total del pedido");
                }
                pedido.setMontoPagado(montoPagado);
                pedido.setEstadoPago("ANTICIPO");
                break;

            case "PAGADO":

                if (montoPagado != totalPedido) {throw new BussinessException("El monto pagado debe ser igual al total del pedido");
                }
                pedido.setMontoPagado(totalPedido);
                pedido.setEstadoPago("PAGADO");
                break;

            default:
                throw new BussinessException("Estado de pago no válido");
        }
    }

    private Map<Long, List<DetalleVenta>> agruparDetallesPorVendedor(VentaModelRequest request) {

        Map<Long, List<DetalleVenta>> detallesPorVendedor = new LinkedHashMap<>();

        for (DetalleVentaModelRequest item : request.getProductos()) {

            Producto producto = obtenerProducto(item.getProductoId());

            validarProducto(producto);

            Vendedor vendedor = producto.getCategoria().getVendedor();

            int subtotalProducto = producto.getPrecio() * item.getCantidad();

            DetalleVenta detalle = ventaMapper.detalleVentaMapper(producto, item, subtotalProducto);

            detallesPorVendedor.computeIfAbsent(vendedor.getId(), key -> new ArrayList<>())
                    .add(detalle);
        }

        return detallesPorVendedor;
    }

    private List<VentaModelResponse> crearVentas(VentaModelRequest request, Map<Long, List<DetalleVenta>> detallesPorVendedor, Pedido pedidoGuardado) {

        List<VentaModelResponse> respuestas = new ArrayList<>();

        for (List<DetalleVenta> detalles : detallesPorVendedor.values()) {

            Vendedor vendedor = detalles.get(0)
                    .getProducto()
                    .getCategoria()
                    .getVendedor();

            int subtotalVenta = calcularSubtotal(detalles);

            int descuento = calcularDescuento(request, vendedor, subtotalVenta);

            int total = subtotalVenta - descuento;

            Venta venta = ventaMapper.mapearVenta(request, vendedor, pedidoGuardado, subtotalVenta, total, descuento);

            Venta ventaGuardada = ventaRepository.save(venta);

            guardarDetalles(detalles, ventaGuardada);

            ventaGuardada.setDetalles(detalles);

            pedidoGuardado.getVentas().add(ventaGuardada);

            respuestas.add(ventaMapper.responseModel(ventaGuardada));
        }

        return respuestas;
    }

    private Producto obtenerProducto(Long productoId) {

        return prodRepository.findById(productoId).orElseThrow(() -> new ResourceNotFoundException("No se encontró el producto con el id: " + productoId));
    }

    private void validarProducto(Producto producto) {

        if (!producto.getActivo()) {
            throw new BussinessException("El producto no está activo: " + producto.getNombre());
        }
    }

    private int calcularSubtotal(List<DetalleVenta> detalles) {

        return detalles.stream()
                .mapToInt(DetalleVenta::getSubtotal)
                .sum();
    }

    private void guardarDetalles(List<DetalleVenta> detalles, Venta venta) {

        for (DetalleVenta detalle : detalles) {
            detalle.setVenta(venta);
        }

        detalleRepository.saveAll(detalles);
    }

    private int calcularDescuento(VentaModelRequest request, Vendedor vendedor, int subtotal) {

        if (request.getDescuentos() == null) {
            return 0;
        }

        return request.getDescuentos()
                .stream()
                .filter(descuento -> descuento.getVendedorId() != null && descuento.getVendedorId().equals(vendedor.getId()))
                .map(DescuentoModelRequest::getDescuento)
                .findFirst()
                .map(descuento -> {
                    if (descuento < 0) {
                        throw new BussinessException("El descuento no puede ser negativo");
                    }

                    if (descuento > subtotal) {
                        throw new BussinessException("El descuento no puede ser mayor al subtotal");
                    }
                    return descuento;
                })
                .orElse(0);
    }

    private int calcularTotalPedido(VentaModelRequest request, Map<Long, List<DetalleVenta>> detallesPorVendedor) {

        int totalPedido = 0;

        for (List<DetalleVenta> detalles : detallesPorVendedor.values()) {

            Vendedor vendedor = detalles.get(0)
                    .getProducto()
                    .getCategoria()
                    .getVendedor();

            int subtotalVenta = calcularSubtotal(detalles);

            int descuento = calcularDescuento(request, vendedor, subtotalVenta);

            totalPedido += subtotalVenta - descuento;
        }

        return totalPedido;
    }
}

