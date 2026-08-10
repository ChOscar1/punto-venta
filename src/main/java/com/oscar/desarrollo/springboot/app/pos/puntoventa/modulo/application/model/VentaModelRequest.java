package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class VentaModelRequest {

    @NotNull
    private Long vendedorId;

    @NotBlank
    private String metodoPago;

    @NotEmpty
    private List<DetalleVentaModelRequest> productos;
}
