package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class VentaModelRequest {

    @NotNull(message = "No debe ser nulo")
    private Long vendedorId;

    @NotBlank(message = "No debe estar vacio")
    private String metodoPago;

    @NotEmpty(message = "No debe estar vacio")
    private List<DetalleVentaModelRequest> productos;
}
