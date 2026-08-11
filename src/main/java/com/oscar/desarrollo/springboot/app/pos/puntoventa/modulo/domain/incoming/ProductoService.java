package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.incoming;

import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.ProductoModelRequest;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.ProductoResponseModel;

import java.util.List;

public interface ProductoService {

    ProductoResponseModel guardar(ProductoModelRequest productoRequest);

    ProductoResponseModel buscarById(Long id);

    ProductoResponseModel actualizar(Long id, ProductoModelRequest productoRequest);

    void eliminarById(Long id);

    List<ProductoResponseModel> listar();
}
