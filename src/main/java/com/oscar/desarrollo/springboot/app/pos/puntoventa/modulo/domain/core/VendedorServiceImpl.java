package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.core;

import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.entity.Categoria;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.entity.Vendedor;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.mapper.VendedorMapper;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.VendedorModelRequest;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.VendedorResponseModel;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.incoming.VendedorService;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.incoming.error.exception.ResourceNotFoundException;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.infraestructure.CategoriaRepository;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.infraestructure.VendedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VendedorServiceImpl implements VendedorService {

    @Autowired
    VendedorRepository vendedorRepositoy;
    @Autowired
    VendedorMapper vendedorMapper;
    @Autowired
    CategoriaRepository categoriaRepository;


    @Override
    @Transactional
    public VendedorResponseModel guardar(VendedorModelRequest vendedorRequest) {

        Vendedor vendedor = vendedorMapper.mapearEntidad(vendedorRequest);

        Vendedor vendedorGuardado = vendedorRepositoy.save(vendedor);

        List<Categoria> categorias = categoriaRepository.findByVendedorId(vendedorGuardado.getId()
                );

        return vendedorMapper.responseMapper(vendedorGuardado, categorias);
    }

    @Override
    @Transactional
    public VendedorResponseModel buscarById(Long id) {

        Vendedor vendedorEncontrado = vendedorRepositoy.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("No se encontro ningun vendedor con el id: "+ id));

        List<Categoria> categorias = categoriaRepository.findByVendedorId(vendedorEncontrado.getId()
                );

        return vendedorMapper.responseMapper(vendedorEncontrado, categorias);
    }

    @Override
    @Transactional
    public VendedorResponseModel actualizar(Long id, VendedorModelRequest vendedorRequest) {

        Vendedor vendedorEncontrado = vendedorRepositoy.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("No se encontro ningun vendedor con el id: "+ id));

        Vendedor actualizado = vendedorMapper.mapearEntidadActualizada(vendedorEncontrado, vendedorRequest);

        Vendedor vendedorActualizado = vendedorRepositoy.save(actualizado);

        List<Categoria> categorias = categoriaRepository.findByVendedorId(vendedorActualizado.getId()
                );
        return vendedorMapper.responseMapper(actualizado, categorias);
    }

    @Override
    @Transactional
    public void eliminarById(Long id) {
        Vendedor vendedorEncontrado = vendedorRepositoy.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("No se encontro ningun vendedor con el id: "+ id));

        vendedorRepositoy.delete(vendedorEncontrado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VendedorResponseModel> listar() {

        return vendedorRepositoy.findAll()
                .stream()
                .map(vendedor -> {

                    List<Categoria> categorias =
                            categoriaRepository
                                    .findByVendedorId(
                                            vendedor.getId()
                                    );

                    return vendedorMapper.responseMapper(
                            vendedor,
                            categorias
                    );

                })
                .toList();
    }
}
