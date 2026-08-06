package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.core;

import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.entity.Vendedor;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.mapper.VendedorMapper;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.VendedorModelRequest;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.VendedorResponseModel;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.incoming.VendedorService;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.infraestructure.VendedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendedorServiceImpl implements VendedorService {

    @Autowired
    VendedorRepository vendedorRepositoy;
    @Autowired
    VendedorMapper vendedorMapper;


    @Override
    public VendedorResponseModel guardar(VendedorModelRequest vendedorRequest) {

        Vendedor vendedor = vendedorMapper.mapearEntidad(vendedorRequest);

        Vendedor vendedorGuardado = vendedorRepositoy.save(vendedor);

        return vendedorMapper.responseMapper(vendedorGuardado);
    }

    @Override
    public VendedorResponseModel buscarById(Long id) {

        Vendedor vendedorEncontrado = vendedorRepositoy.findById(id).orElseThrow(()
                -> new RuntimeException("No se encontro ningun vendedor con el id: "+ id));

        return vendedorMapper.responseMapper(vendedorEncontrado);
    }

    @Override
    public VendedorResponseModel actualizar(Long id, VendedorModelRequest vendedorRequest) {

        Vendedor vendedorEncontrado = vendedorRepositoy.findById(id).orElseThrow(()
                -> new RuntimeException("No se encontro ningun vendedor con el id: "+ id));

        Vendedor actualizado = vendedorMapper.mapearEntidadActualizada(vendedorEncontrado, vendedorRequest);

        vendedorRepositoy.save(actualizado);
        return vendedorMapper.responseMapper(actualizado);
    }

    @Override
    public void eliminarById(Long id) {
        Vendedor vendedorEncontrado = vendedorRepositoy.findById(id).orElseThrow(()
                -> new RuntimeException("No se encontro ningun vendedor con el id: "+ id));

        vendedorRepositoy.delete(vendedorEncontrado);
    }

    @Override
    public List<VendedorResponseModel> listar() {
        return vendedorRepositoy.findAll()
                .stream()
                .map(vendedorMapper::responseMapper)
                .toList();
    }
}
