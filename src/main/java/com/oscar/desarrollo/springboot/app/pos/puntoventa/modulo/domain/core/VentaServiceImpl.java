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
        pedido.setFecha(
                LocalDateTime.now(
                        ZoneId.of("America/Mexico_City")
                )
        );
        pedido.setEstado("PENDIENTE");

        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        Map<Long, List<DetalleVenta>> detallesPorVendedor = agruparDetallesPorVendedor(request);

        crearVentas(request, detallesPorVendedor, pedidoGuardado);

        return pedidoMapper.responseModel(pedidoGuardado);
    }

/*    @Override
    @Transactional
    public VentaModelResponse entregarVenta(Long id) {

        Venta venta = ventaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No se encontró la venta con el id: " + id));

        if (venta.getEstado().equals("Entregada")) {
            throw new BussinessException("La venta ya fue entregada");
        }

        venta.setEstado("Entregada");
        Venta guardarVenta = ventaRepository.save(venta);
        return ventaMapper.responseModel(guardarVenta);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VentaModelResponse> listarPendientes() {

        return ventaRepository.findByEstado("Pendiente")
                .stream()
                .map(ventaMapper::responseModel)
                .toList();
    }*/

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
}

