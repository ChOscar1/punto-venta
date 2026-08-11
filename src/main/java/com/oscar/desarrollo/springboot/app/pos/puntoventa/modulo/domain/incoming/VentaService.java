package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.incoming;

import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.VentaModelRequest;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.VentaModelResponse;

import java.util.List;

public interface VentaService {

    List<VentaModelResponse> registrarVenta(VentaModelRequest request);

    VentaModelResponse buscarById(Long id);

    List<VentaModelResponse> listar();
}
