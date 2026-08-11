package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.mapper;

import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.entity.Categoria;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.entity.Vendedor;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.CategoriaResponseModel;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.VendedorModelRequest;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.VendedorResponseModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VendedorMapper {

    public Vendedor mapearEntidad(VendedorModelRequest vendedorModelRequest) {

        Vendedor vendedor = new Vendedor();
        vendedor.setNombre(vendedorModelRequest.getNombre());

        return vendedor;
    }

    public VendedorResponseModel responseMapper(Vendedor vendedor, List<Categoria> categorias) {

        VendedorResponseModel response = new VendedorResponseModel();
        response.setId(vendedor.getId());
        response.setNombre(vendedor.getNombre());
        response.setCategorias(
                categorias.stream()
                        .map(this::categoriaResponseMapper)
                        .toList()
        );

        return response;
    }

    private CategoriaResponseModel categoriaResponseMapper(
            Categoria categoria
    ) {

        CategoriaResponseModel response = new CategoriaResponseModel();

        response.setId(categoria.getId());

        response.setNombre(categoria.getNombre());

        response.setVendedor(categoria.getVendedor().getNombre());

        return response;
    }

    public Vendedor mapearEntidadActualizada(Vendedor vendedor, VendedorModelRequest vendedorModelRequest) {

        vendedor.setNombre(vendedorModelRequest.getNombre());

        return vendedor;
    }
}
