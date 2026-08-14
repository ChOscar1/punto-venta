package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class VentaModelRequest {

    @NotBlank
    private String nombreCliente;

    @NotBlank(message = "No debe estar vacio")
    private String metodoPago;

    @NotEmpty(message = "No debe estar vacio")
    private List<DetalleVentaModelRequest> productos;

    private Long vendedorDescuentoId;

    @Min(message = "El valor debe ser igual o mayor a 0", value = 0)
    private Integer descuento;
}
