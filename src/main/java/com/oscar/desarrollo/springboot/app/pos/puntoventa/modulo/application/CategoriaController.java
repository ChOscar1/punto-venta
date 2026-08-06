package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application;

import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.entity.Categoria;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.CategoriaModelRequest;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.CategoriaResponseModel;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.incoming.CategoriaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/punto-venta/categorias")
@Slf4j
public class CategoriaController {

    @Autowired
    CategoriaService categoria;

    @PostMapping("/crear-categoria")
    public ResponseEntity<CategoriaResponseModel> crear(@RequestBody CategoriaModelRequest request) {

        return ResponseEntity.ok(categoria.guardar(request));
    }

    @GetMapping("/categoria/{id}")
    public ResponseEntity<CategoriaResponseModel> buscarCategoriaId(@PathVariable Long id) {

        return ResponseEntity.ok(categoria.buscarById(id));
    }

    @PutMapping("/actualizar-categoria/{id}")
    public ResponseEntity<CategoriaResponseModel> actualizar(@PathVariable Long id, @RequestBody CategoriaModelRequest request) {

        return ResponseEntity.ok(categoria.actualizar(id, request));
    }

    @DeleteMapping("/eliminar-categoria/{id}")
    public void eliminarPorId(@PathVariable Long id) {
        categoria.eliminarById(id);
        log.info("Se elimino correctamente la categoria con ID: {}", id);
    }

    @GetMapping("/listar-categorias")
    public ResponseEntity<List<CategoriaResponseModel>>listarCategorias() {

        return ResponseEntity.ok(categoria.listar());
    }
}
