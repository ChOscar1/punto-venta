package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model;

import lombok.Data;

import java.util.List;

@Data
public class VendedorResponseModel {

    private Long id;

    private String nombre;

    private List<CategoriaResponseModel> categorias;
}
