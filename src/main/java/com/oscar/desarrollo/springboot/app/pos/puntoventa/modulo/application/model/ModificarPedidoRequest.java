package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class ModificarPedidoRequest {

    @NotEmpty(message = "No debe estar vacio")
    private List<ModificarVentaRequest> ventas;
}
