package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductoModelRequest {

    @NotBlank
    private String nombre;

    @NotNull
    private Long categoriaId;

    @NotNull
    private Integer precio;

    @NotNull
    private Boolean activo;
}
