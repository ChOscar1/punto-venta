package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.incoming;

import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.VendedorModelRequest;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.VendedorResponseModel;

import java.util.List;

public interface VendedorService {

    VendedorResponseModel guardar(VendedorModelRequest vendedor);

    VendedorResponseModel buscarById(Long id);

    VendedorResponseModel actualizar(Long id, VendedorModelRequest vendedor);

    void eliminarById(Long id);

    List<VendedorResponseModel> listar();
}
