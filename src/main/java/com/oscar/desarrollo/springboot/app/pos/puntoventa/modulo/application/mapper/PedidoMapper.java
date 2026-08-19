package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.mapper;

import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.entity.Pedido;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.PedidoModelResponse;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.VentaModelResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PedidoMapper {

    @Autowired
    VentaMapper ventaMapper;

    public PedidoModelResponse responseModel(Pedido pedido) {

        PedidoModelResponse response = new PedidoModelResponse();

        response.setId(pedido.getId());
        response.setFecha(pedido.getFecha());
        response.setNombreCliente(pedido.getNombreCliente());
        response.setEstado(pedido.getEstado());

        List<VentaModelResponse> ventas = pedido.getVentas()
                        .stream()
                        .map(ventaMapper::responseModel)
                        .toList();

        response.setVentas(ventas);

        int total = ventas.stream()
                .mapToInt(VentaModelResponse::getTotal)
                .sum();

        response.setTotal(total);

        response.setMontoPagado(pedido.getMontoPagado());

        response.setEstadoPago(pedido.getEstadoPago());

        response.setMontoPendiente(total - pedido.getMontoPagado());

        return response;
    }
}