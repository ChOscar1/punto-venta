package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model;

import lombok.Data;

@Data
public class DetalleVentaResponse {

    private String producto;

    private Integer cantidad;

    private Integer precioUnitario;

    private Integer subtotal;
}
