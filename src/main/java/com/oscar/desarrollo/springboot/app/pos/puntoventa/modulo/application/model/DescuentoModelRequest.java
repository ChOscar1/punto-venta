package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class DescuentoModelRequest {

    private Long vendedorId;

    @Min(value = 0, message = "El descuento no puede ser negativo")
    private Integer descuento;
}