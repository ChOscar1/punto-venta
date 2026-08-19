package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PedidoModelResponse {

    private Long id;

    private LocalDateTime fecha;

    private String nombreCliente;

    private String estado;

    private List<VentaModelResponse> ventas;

    private Integer total;

    private Integer montoPagado;

    private String estadoPago;

    private Integer montoPendiente;

}