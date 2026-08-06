package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.core;

import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.entity.Categoria;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.entity.Producto;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.mapper.ProductoMapper;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.ProductoModelRequest;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.incoming.ProductoService;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.infraestructure.CategoriaRepository;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.infraestructure.ProductoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    ProductoRepository productoRepository;
    @Autowired
    CategoriaRepository categoriaRepository;
    @Autowired
    ProductoMapper productoMapper;

    @Override
    public Producto guardar(ProductoModelRequest productoRequest) {

        Categoria categoria = categoriaRepository.findById(productoRequest.getCategoriaId()).orElseThrow(() ->
                new RuntimeException("No se encontro el id de la categoria: " + productoRequest.getCategoriaId()));

        Producto producto = productoMapper.mapearEntidad(productoRequest, categoria);

        return productoRepository.save(producto);
    }

    @Override
    public Producto buscarById(Long id) {

        return productoRepository.findById(id).orElseThrow(() -> new RuntimeException("No se encontro ningun registro con el id: " + id));
    }

    @Override
    public Producto actualizar(Long id, ProductoModelRequest productoRequest) {

        Producto productoExiste = productoRepository.findById(id).orElseThrow(() -> new RuntimeException("No se encontro ningun registro con el id: " + id));
        Categoria categoriaExiste = categoriaRepository.findById(productoRequest.getCategoriaId()).orElseThrow(() ->
                new RuntimeException("No se encontro el id de la categoria: " + productoRequest.getCategoriaId()));

        return productoRepository.save(productoMapper.mapearEntidadActualizada(productoExiste, productoRequest, categoriaExiste));
    }

    @Override
    public void eliminarById(Long id) {
        Producto productoExiste = productoRepository.findById(id).orElseThrow(() -> new RuntimeException("No se encontro ningun registro con el id: " + id));
        productoRepository.delete(productoExiste);
    }

    @Override
    public List<Producto> listar() {
        return productoRepository.findAll();
    }
}
