package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.incoming;

import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.entity.Producto;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.ProductoModelRequest;

import java.util.List;

public interface ProductoService {

    Producto guardar(ProductoModelRequest productoRequest);

    Producto buscarById(Long id);

    Producto actualizar(Long id, ProductoModelRequest productoRequest);

    void eliminarById(Long id);

    List<Producto> listar();
}
