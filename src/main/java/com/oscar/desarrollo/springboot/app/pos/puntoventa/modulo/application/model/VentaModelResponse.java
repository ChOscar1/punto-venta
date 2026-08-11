package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class VentaModelResponse {

    private Long id;

    private LocalDateTime fecha;

    private String vendedor;

    private Integer subtotal;

    private Integer descuento;

    private Integer total;

    private String metodoPago;

    private List<DetalleVentaResponse> productos;
}
