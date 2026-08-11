package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application;


import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.VendedorModelRequest;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.VendedorResponseModel;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.incoming.VendedorService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/punto-venta/vendedores")
@Slf4j
public class VendedorController {

    @Autowired
    VendedorService vendedorService;

    @PostMapping("/crear-vendedor")
    public ResponseEntity<VendedorResponseModel> crearProducto(@RequestBody @Valid VendedorModelRequest request) {

        return ResponseEntity.ok(vendedorService.guardar(request));
    }

    @GetMapping("/obtener-vendedor/{id}")
    public ResponseEntity<VendedorResponseModel> obtenerProducto(@PathVariable Long id) {

        return ResponseEntity.ok(vendedorService.buscarById(id));
    }

    @PutMapping("/actualizar-vendedor/{id}")
    public ResponseEntity<VendedorResponseModel> actualizarProducto(@PathVariable Long id, @RequestBody @Valid VendedorModelRequest request) {

        return ResponseEntity.ok(vendedorService.actualizar(id, request));
    }

    @DeleteMapping("/eliminar-vendedor/{id}")
    public void eliminarProducto(@PathVariable Long id) {

        vendedorService.eliminarById(id);
    }

    @GetMapping("/obtener-vendedores")
    public ResponseEntity<List<VendedorResponseModel>> obtenerProducto() {

        return ResponseEntity.ok(vendedorService.listar());
    }
}
