package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application;

import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.PedidoModelResponse;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.incoming.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/punto-venta/pedidos")
public class PedidoController {

    @Autowired
    PedidoService pedidoService;

    @PutMapping("/entregar-pedido/{id}")
    public ResponseEntity<PedidoModelResponse> entregarPedido(@PathVariable Long id) {

        return ResponseEntity.ok(
                pedidoService.entregarPedido(id)
        );
    }

    @GetMapping("/obtener-pedidos-pendientes")
    public ResponseEntity<List<PedidoModelResponse>> listarPendientes() {

        return ResponseEntity.ok(pedidoService.listarPendientes());
    }

    @PutMapping("/{id}/cancelar")
    public PedidoModelResponse cancelarPedido(@PathVariable Long id) {
        return pedidoService.cancelarPedido(id);
    }

    @PutMapping("/{id}/registrar-pago")
    public PedidoModelResponse registrarPago(@PathVariable Long id, @RequestBody Integer monto) {
        return pedidoService.registrarPago(id, monto);
    }
}