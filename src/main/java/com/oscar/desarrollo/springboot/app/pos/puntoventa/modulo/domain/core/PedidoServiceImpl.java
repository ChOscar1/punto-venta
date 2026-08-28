package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.core;

import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.entity.DetalleVenta;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.entity.Pedido;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.entity.Producto;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.entity.Venta;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.mapper.PedidoMapper;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.mapper.VentaMapper;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.ModificarDetalleVentaRequest;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.ModificarPedidoRequest;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.ModificarVentaRequest;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.PedidoModelResponse;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.incoming.PedidoService;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.incoming.error.exception.BussinessException;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.incoming.error.exception.ResourceNotFoundException;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.infraestructure.PedidoRepository;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.infraestructure.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    PedidoRepository pedidoRepository;
    @Autowired
    PedidoMapper pedidoMapper;
    @Autowired
    ProductoRepository productoRepository;
    @Autowired
    VentaMapper ventaMapper;

    @Transactional
    public PedidoModelResponse entregarPedido(Long id) {

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No se encontró el pedido con el id: " + id
                        ));

        if ("ENTREGADO".equals(pedido.getEstado())) {
            throw new BussinessException("El pedido ya fue entregado");
        }

        pedido.setEstado("ENTREGADO");

        pedidoRepository.save(pedido);

        return pedidoMapper.responseModel(pedido);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoModelResponse> listarPendientes() {

        return pedidoRepository.findByEstado("PENDIENTE")
                .stream()
                .map(pedidoMapper::responseModel)
                .toList();
    }

    @Transactional
    @Override
    public PedidoModelResponse cancelarPedido(Long id) {

        Pedido pedido = pedidoRepository.findById(id).orElseThrow(() ->
                        new ResourceNotFoundException("No se encontró el pedido con el id: " + id));

        if ("ENTREGADO".equals(pedido.getEstado())) {
            throw new BussinessException("No se puede cancelar un pedido que ya fue entregado");
        }

        if ("CANCELADO".equals(pedido.getEstado())) {
            throw new BussinessException("El pedido ya fue cancelado");
        }

        pedido.setEstado("CANCELADO");

        pedidoRepository.save(pedido);

        return pedidoMapper.responseModel(pedido);
    }

    @Override
    @Transactional
    public PedidoModelResponse registrarPago(Long id, Integer monto) {

        Pedido pedido = pedidoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No se encontró el pedido con id: " + id));

        if (monto == null || monto <= 0) {
            throw new BussinessException("El monto del pago debe ser mayor a 0");
        }

        int totalPedido = pedido.getVentas()
                .stream()
                .mapToInt(Venta::getTotal)
                .sum();

        int montoPagadoActual = pedido.getMontoPagado() != null ? pedido.getMontoPagado() : 0;

        int montoPendiente = totalPedido - montoPagadoActual;

        if (monto > montoPendiente) {
            throw new BussinessException("El pago no puede ser mayor al monto pendiente");
        }

        int nuevoMontoPagado = montoPagadoActual + monto;

        int nuevoMontoPendiente = totalPedido - nuevoMontoPagado;

        pedido.setMontoPagado(nuevoMontoPagado);

        if (nuevoMontoPendiente == 0) {

            pedido.setEstadoPago("PAGADO");

        } else {

            pedido.setEstadoPago("ANTICIPO");
        }

        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        return pedidoMapper.responseModel(pedidoGuardado);
    }

    @Override
    @Transactional
    public PedidoModelResponse modificarPedido(Long id, ModificarPedidoRequest request) {

        Pedido pedido = pedidoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No se encontró el pedido con id: " + id));

        if (!"PENDIENTE".equals(pedido.getEstado())) {

            throw new BussinessException("Solo se pueden modificar pedidos pendientes");
        }

        for (ModificarVentaRequest ventaRequest : request.getVentas()) {

            Venta venta = pedido.getVentas().stream()
                    .filter(v -> v.getId().equals(ventaRequest.getVentaId()))
                    .findFirst()
                    .orElseThrow(() -> new BussinessException("La venta con id " + ventaRequest.getVentaId() + " no pertenece al pedido"));


            venta.setMetodoPago(ventaRequest.getMetodoPago());

            if (ventaRequest.getProductos() == null || ventaRequest.getProductos().isEmpty()) {

                throw new BussinessException("La venta debe tener al menos un producto");
            }

            venta.getDetalles().clear();

            int subtotal = 0;
g
            for (ModificarDetalleVentaRequest detalleRequest : ventaRequest.getProductos()) {

                Producto producto = productoRepository.findById(detalleRequest.getProductoId())
                        .orElseThrow(() -> new ResourceNotFoundException("No se encontró el producto con id: " + detalleRequest.getProductoId()));

                if (!producto.getCategoria().getVendedor().getId().equals(venta.getVendedor().getId())) {

                    throw new BussinessException("El producto " + producto.getNombre() + " no pertenece al vendedor " + venta.getVendedor().getNombre());
                }

                DetalleVenta detalle = ventaMapper.detalleVentaModificarMapper(producto, detalleRequest, venta);

                venta.getDetalles().add(detalle);

                subtotal += detalle.getSubtotal();

            }

            int descuento = venta.getDescuento() != null ? venta.getDescuento() : 0;

            int total = subtotal - descuento;

            if (total < 0) {
                throw new BussinessException("El descuento no puede ser mayor al subtotal");
            }

            venta.setSubtotal(subtotal);
            venta.setTotal(total);
        }

        actualizarEstadoPago(pedido);

        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        return pedidoMapper.responseModel(pedidoGuardado);
    }

    private void actualizarEstadoPago(Pedido pedido) {

        int totalPedido = pedido.getVentas()
                .stream()
                .mapToInt(Venta::getTotal)
                .sum();

        int montoPagado = pedido.getMontoPagado() != null ? pedido.getMontoPagado() : 0;

        if (montoPagado > totalPedido) {
            throw new BussinessException("El monto pagado no puede ser mayor al nuevo total del pedido");
        }

        int pendiente = totalPedido - montoPagado;

        if (pendiente == 0) {

            pedido.setEstadoPago("PAGADO");

        } else if (montoPagado > 0) {

            pedido.setEstadoPago("ANTICIPO");

        } else {

            pedido.setEstadoPago("SIN_PAGAR");
        }
    }
}

