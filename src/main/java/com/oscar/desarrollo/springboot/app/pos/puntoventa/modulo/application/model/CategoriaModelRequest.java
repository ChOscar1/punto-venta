package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoriaModelRequest {

    @NotBlank(message = "No debe estar vacio")
    private String nombre;

    @NotNull(message = "No debe ser nulo")
    private Long vendedorId;
}
