package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class VentaModelRequest {

    @NotBlank(message = "No debe estar vacio")
    private String metodoPago;

    @NotEmpty(message = "No debe estar vacio")
    private List<DetalleVentaModelRequest> productos;
}
