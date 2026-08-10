package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DetalleVentaModelRequest {

    @NotNull
    private Long productoId;

    @NotNull
    @Min(1)
    private Integer cantidad;
}
