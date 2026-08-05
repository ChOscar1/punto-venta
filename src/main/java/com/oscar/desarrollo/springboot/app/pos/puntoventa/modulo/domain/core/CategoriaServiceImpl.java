package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.core;

import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.entity.Categoria;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.CategoriaModelRequest;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.incoming.CategoriaService;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.infraestructure.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    @Autowired
    CategoriaRepository categoriaRepository;

    @Override
    public Categoria guardar(CategoriaModelRequest categoriaModelRequest) {
        Categoria categoria = new Categoria();
        categoria.setNombre(categoriaModelRequest.getNombre());

        return categoriaRepository.save(categoria);
    }

    @Override
    public Categoria buscarById(Long id) {
        return categoriaRepository.findById(id).orElseThrow(() -> new RuntimeException("categoria no encontrada"));
    }

    @Override
    @Transactional
    public Categoria actualizar(Long id, CategoriaModelRequest categoriaModelRequest) {
        Categoria existente = categoriaRepository.findById(id).orElseThrow(() -> new RuntimeException("categoria no encontrada con el id: " + id));

        existente.setNombre(categoriaModelRequest.getNombre());

        return categoriaRepository.save(existente);

    }

    @Override
    public void eliminarById(Long id) {
        Categoria existente = categoriaRepository.findById(id).orElseThrow(() -> new RuntimeException("categoria no encontrada"));

        categoriaRepository.delete(existente);
    }

    @Override
    public List<Categoria> listar() {
        return categoriaRepository.findAll();
    }
}
