package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.mapper;

import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.entity.*;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.DetalleVentaModelRequest;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.DetalleVentaResponse;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.VentaModelRequest;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.VentaModelResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class VentaMapper {

    public Venta mapearVenta(VentaModelRequest request, Vendedor vendedor, Pedido pedido, int subTotal, int totalVenta, int descuento) {

        Venta venta = new Venta();
        venta.setMetodoPago(request.getMetodoPago());
        venta.setFecha(LocalDateTime.now(ZoneId.of("America/Mexico_City")));
        venta.setPedido(pedido);
        venta.setVendedor(vendedor);
        venta.setSubtotal(subTotal);
        venta.setDescuento(descuento);
        venta.setTotal(totalVenta);
        return venta;
    }

    public VentaModelResponse responseModel(Venta venta) {

        VentaModelResponse response = new VentaModelResponse();

        response.setId(venta.getId());
        response.setFecha(venta.getFecha());
        response.setMetodoPago(venta.getMetodoPago());
        response.setSubtotal(venta.getSubtotal());
        response.setDescuento(venta.getDescuento());
        response.setTotal(venta.getTotal());
        response.setVendedor(venta.getVendedor().getNombre());

        response.setProductos(
                venta.getDetalles()
                        .stream()
                        .map(this::detalleResponse)
                        .toList()
        );

        return response;
    }

    public DetalleVenta detalleVentaMapper(Producto producto, DetalleVentaModelRequest item, int total) {

        DetalleVenta detalle = new DetalleVenta();

        detalle.setProducto(producto);
        detalle.setCantidad(item.getCantidad());
        detalle.setPrecioUnitario(producto.getPrecio());
        detalle.setSubtotal(total);

        return detalle;
    }

    private DetalleVentaResponse detalleResponse(DetalleVenta detalle) {

        DetalleVentaResponse response = new DetalleVentaResponse();

        response.setProducto(detalle.getProducto().getNombre());
        response.setCantidad(detalle.getCantidad());
        response.setPrecioUnitario(detalle.getPrecioUnitario());
        response.setSubtotal(detalle.getSubtotal());

        return response;
    }

}
