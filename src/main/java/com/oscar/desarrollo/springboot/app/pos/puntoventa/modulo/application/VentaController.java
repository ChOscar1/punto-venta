package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application;

import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.VentaModelRequest;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.VentaModelResponse;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.incoming.VentaService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/punto-venta/ventas")
@Slf4j
public class VentaController {

    @Autowired
    VentaService ventaService;

    @PostMapping("/crear-venta")
    public ResponseEntity<List<VentaModelResponse>> registrarVenta(@RequestBody @Valid VentaModelRequest request) {

        return ResponseEntity.ok(ventaService.registrarVenta(request));
    }

    @GetMapping("/obtener/{id}")
    public ResponseEntity<VentaModelResponse> buscarPorId(@PathVariable Long id) {

        return ResponseEntity.ok(ventaService.buscarById(id));
    }

    @GetMapping("/obtener-ventas")
    public ResponseEntity<List<VentaModelResponse>> listar() {

        return ResponseEntity.ok(ventaService.listar());
    }
}
