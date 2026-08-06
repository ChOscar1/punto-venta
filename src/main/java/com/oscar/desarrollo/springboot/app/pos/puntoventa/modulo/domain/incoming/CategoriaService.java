package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.incoming;


import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.entity.Categoria;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.CategoriaModelRequest;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.CategoriaResponseModel;

import java.util.List;

public interface CategoriaService {

    CategoriaResponseModel guardar(CategoriaModelRequest categoriaModelRequest);

    CategoriaResponseModel buscarById(Long id);

    CategoriaResponseModel actualizar(Long id, CategoriaModelRequest categoriaModelRequest);

    void eliminarById(Long id);

    List<CategoriaResponseModel> listar();
}
