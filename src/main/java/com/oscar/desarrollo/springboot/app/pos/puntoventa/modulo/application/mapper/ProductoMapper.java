package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.mapper;

import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.entity.Categoria;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.entity.Producto;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.ProductoModelRequest;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.ProductoResponseModel;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    public Producto mapearEntidad(ProductoModelRequest productoRequest, Categoria categoria) {

        Producto producto = new Producto();

        producto.setActivo(productoRequest.getActivo());
        producto.setCategoria(categoria);
        producto.setPrecio(productoRequest.getPrecio());
        producto.setNombre(productoRequest.getNombre());

        return producto;
    }

    public ProductoResponseModel mapperResponse(Producto producto) {

        ProductoResponseModel response = new ProductoResponseModel();

        response.setId(producto.getId());
        response.setActivo(producto.getActivo());
        response.setCategoriaId(producto.getCategoria().getId());
        response.setPrecio(producto.getPrecio());
        response.setNombre(producto.getNombre());

        return response;
    }

    public Producto mapearEntidadActualizada(Producto producto, ProductoModelRequest productoRequest, Categoria categoria) {

        producto.setActivo(productoRequest.getActivo());
        producto.setCategoria(categoria);
        producto.setPrecio(productoRequest.getPrecio());
        producto.setNombre(productoRequest.getNombre());

        return producto;
    }
}
