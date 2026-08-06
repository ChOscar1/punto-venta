package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application;

import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.ProductoModelRequest;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.ProductoResponseModel;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.incoming.ProductoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/punto-venta/productos")
@Slf4j
public class ProductoController {

    @Autowired
    ProductoService producto;

    @PostMapping("/crear-producto")
    public ResponseEntity<ProductoResponseModel> crearProducto(@RequestBody ProductoModelRequest request) {

        return ResponseEntity.ok(producto.guardar(request));
    }

    @GetMapping("/obtener-producto/{id}")
    public ResponseEntity<ProductoResponseModel> obtenerProducto(@PathVariable Long id) {

        return ResponseEntity.ok(producto.buscarById(id));
    }

    @PutMapping("/actualizar-producto/{id}")
    public ResponseEntity<ProductoResponseModel> actualizarProducto(@PathVariable Long id, @RequestBody ProductoModelRequest request) {

        return ResponseEntity.ok(producto.actualizar(id, request));
    }

    @DeleteMapping("/eliminar-producto/{id}")
    public void eliminarProducto(@PathVariable Long id) {

        producto.eliminarById(id);
    }

    @GetMapping("/obtener-productos")
    public ResponseEntity<List<ProductoResponseModel>> obtenerProducto() {

        return ResponseEntity.ok(producto.listar());
    }
}
