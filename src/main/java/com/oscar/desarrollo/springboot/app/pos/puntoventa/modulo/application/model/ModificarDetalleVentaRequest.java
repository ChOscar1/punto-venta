package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ModificarDetalleVentaRequest {

    @NotNull
    private Long productoId;

    @NotNull
    @Positive
    private Integer cantidad;
}
