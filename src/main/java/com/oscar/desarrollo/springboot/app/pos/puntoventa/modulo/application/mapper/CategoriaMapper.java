package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.mapper;

import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.entity.Categoria;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.entity.Vendedor;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.CategoriaModelRequest;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.CategoriaResponseModel;
import org.springframework.stereotype.Component;

@Component
public class CategoriaMapper {

    public Categoria mapearEntidad(CategoriaModelRequest request, Vendedor vendedor) {

        Categoria categoria = new Categoria();
        categoria.setNombre(request.getNombre());
        categoria.setVendedor(vendedor);

        return categoria;
    }

    public Categoria actualizarEntidad(Categoria categoria, CategoriaModelRequest request, Vendedor vendedor) {

        categoria.setNombre(request.getNombre());
        categoria.setVendedor(vendedor);

        return categoria;
    }

    public CategoriaResponseModel responseModel(Categoria categoria) {

        CategoriaResponseModel categoriaModel = new CategoriaResponseModel();

        categoriaModel.setId(categoria.getId());
        categoriaModel.setNombre(categoria.getNombre());
        String obtenerVendedor = String.valueOf(categoria.getVendedor().getNombre());
        categoriaModel.setVendedor(obtenerVendedor);

        return categoriaModel;
    }
}
