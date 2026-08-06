package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.mapper;

import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.entity.Vendedor;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.VendedorModelRequest;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.VendedorResponseModel;
import org.springframework.stereotype.Component;

@Component
public class VendedorMapper {

    public Vendedor mapearEntidad(VendedorModelRequest vendedorModelRequest) {

        Vendedor vendedor = new Vendedor();
        vendedor.setNombre(vendedorModelRequest.getNombre());

        return vendedor;
    }

    public VendedorResponseModel responseMapper(Vendedor vendedor) {

        VendedorResponseModel response = new VendedorResponseModel();
        response.setNombre(vendedor.getNombre());

        return response;
    }

    public Vendedor mapearEntidadActualizada(Vendedor vendedor, VendedorModelRequest vendedorModelRequest) {

        vendedor.setNombre(vendedorModelRequest.getNombre());

        return vendedor;
    }
}
