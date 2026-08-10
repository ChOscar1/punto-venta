package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductoModelRequest {

    @NotBlank(message = "No debe estar vacio")
    private String nombre;

    @NotNull(message = "No debe ser nulo")
    private Long categoriaId;

    @NotNull(message = "No debe ser nulo")
    private Integer precio;

    @NotNull(message = "No debe ser nulo")
    private Boolean activo;
}
