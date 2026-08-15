package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.core;

import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.entity.Pedido;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.mapper.PedidoMapper;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.PedidoModelResponse;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.incoming.PedidoService;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.incoming.error.exception.BussinessException;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.incoming.error.exception.ResourceNotFoundException;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.infraestructure.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    PedidoRepository pedidoRepository;
    @Autowired
    PedidoMapper pedidoMapper;

    @Transactional
    public PedidoModelResponse entregarPedido(Long id) {

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No se encontró el pedido con el id: " + id
                        ));

        if ("ENTREGADO".equals(pedido.getEstado())) {
            throw new BussinessException("El pedido ya fue entregado");
        }

        pedido.setEstado("ENTREGADO");

        pedidoRepository.save(pedido);

        return pedidoMapper.responseModel(pedido);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoModelResponse> listarPendientes() {

        return pedidoRepository.findByEstado("PENDIENTE")
                .stream()
                .map(pedidoMapper::responseModel)
                .toList();
    }

    @Transactional
    @Override
    public PedidoModelResponse cancelarPedido(Long id) {

        Pedido pedido = pedidoRepository.findById(id).orElseThrow(() ->
                        new ResourceNotFoundException("No se encontró el pedido con el id: " + id));

        if ("ENTREGADO".equals(pedido.getEstado())) {
            throw new BussinessException("No se puede cancelar un pedido que ya fue entregado");
        }

        if ("CANCELADO".equals(pedido.getEstado())) {
            throw new BussinessException("El pedido ya fue cancelado");
        }

        pedido.setEstado("CANCELADO");

        pedidoRepository.save(pedido);

        return pedidoMapper.responseModel(pedido);
    }
}
