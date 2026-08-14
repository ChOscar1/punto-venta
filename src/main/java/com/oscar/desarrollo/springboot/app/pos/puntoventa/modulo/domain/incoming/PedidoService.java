package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.incoming;

import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.PedidoModelResponse;

import java.util.List;

public interface PedidoService {

    PedidoModelResponse entregarPedido(Long id);

    List<PedidoModelResponse> listarPendientes();
}
