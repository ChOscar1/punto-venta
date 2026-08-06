package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.core;

import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.entity.Categoria;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.entity.Vendedor;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.mapper.CategoriaMapper;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.CategoriaModelRequest;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.CategoriaResponseModel;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.incoming.CategoriaService;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.infraestructure.CategoriaRepository;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.infraestructure.VendedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    @Autowired
    CategoriaRepository categoriaRepository;
    @Autowired
    VendedorRepository vendedorRepository;
    @Autowired
    CategoriaMapper categoriaMapper;

    @Override
    public CategoriaResponseModel guardar(CategoriaModelRequest categoriaModelRequest) {

        Vendedor vendedor = vendedorRepository.findById(categoriaModelRequest.getVendedorId()).orElseThrow(()
                -> new RuntimeException("No se encontró el vendedor con el id: " + categoriaModelRequest.getVendedorId()));

        Categoria categoria = categoriaMapper.mapearEntidad(categoriaModelRequest, vendedor);
        Categoria categoriaGuardada = categoriaRepository.save(categoria);

        return categoriaMapper.responseModel(categoriaGuardada);
    }

    @Override
    public CategoriaResponseModel buscarById(Long id) {
        Categoria categoriaEncontrada = categoriaRepository.findById(id).orElseThrow(() -> new RuntimeException("categoria no encontrada"));
        return categoriaMapper.responseModel(categoriaEncontrada);
    }

    @Override
    public CategoriaResponseModel actualizar(Long id, CategoriaModelRequest categoriaModelRequest) {

        Categoria existente = categoriaRepository.findById(id).orElseThrow(() -> new RuntimeException("categoria no encontrada con el id: " + id));

        Vendedor vendedor = vendedorRepository.findById(categoriaModelRequest.getVendedorId()).orElseThrow(()
                -> new RuntimeException("No se encontró el vendedor con el id: " + categoriaModelRequest.getVendedorId()));

        categoriaMapper.actualizarEntidad(existente, categoriaModelRequest, vendedor);

        Categoria categoriaGuardada = categoriaRepository.save(existente);

        return categoriaMapper.responseModel(categoriaGuardada);

    }

    @Override
    public void eliminarById(Long id) {
        Categoria existente = categoriaRepository.findById(id).orElseThrow(() -> new RuntimeException("categoria no encontrada"));

        categoriaRepository.delete(existente);
    }

    @Override
    public List<CategoriaResponseModel> listar() {
        return categoriaRepository.findAll()
                .stream()
                .map(categoriaMapper::responseModel)
                .toList();
    }
}
