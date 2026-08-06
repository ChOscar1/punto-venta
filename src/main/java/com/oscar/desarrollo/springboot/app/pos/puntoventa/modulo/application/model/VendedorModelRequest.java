package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VendedorModelRequest {

    @NotBlank
    private String nombre;
}
