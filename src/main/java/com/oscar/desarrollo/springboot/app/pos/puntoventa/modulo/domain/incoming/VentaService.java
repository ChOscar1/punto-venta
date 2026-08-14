package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.incoming;

import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.PedidoModelResponse;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.VentaModelRequest;

public interface VentaService {

    PedidoModelResponse registrarVenta(VentaModelRequest request);

}
