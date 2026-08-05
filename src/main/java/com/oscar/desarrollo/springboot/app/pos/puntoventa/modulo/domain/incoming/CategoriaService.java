package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.incoming;


import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.entity.Categoria;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.CategoriaModelRequest;

import java.util.List;

public interface CategoriaService {

    Categoria guardar(CategoriaModelRequest categoriaModelRequest);

    Categoria buscarById(Long id);

    Categoria actualizar(Long id, CategoriaModelRequest categoriaModelRequest);

    void eliminarById(Long id);

    List<Categoria> listar();
}
