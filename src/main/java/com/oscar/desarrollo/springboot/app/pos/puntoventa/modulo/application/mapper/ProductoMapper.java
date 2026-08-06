package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.mapper;

import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.entity.Categoria;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.entity.Producto;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.ProductoModelRequest;
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

    public Producto mapearEntidadActualizada(Producto producto, ProductoModelRequest productoRequest, Categoria categoria) {

        producto.setActivo(productoRequest.getActivo());
        producto.setCategoria(categoria);
        producto.setPrecio(productoRequest.getPrecio());
        producto.setNombre(productoRequest.getNombre());

        return producto;
    }
}
