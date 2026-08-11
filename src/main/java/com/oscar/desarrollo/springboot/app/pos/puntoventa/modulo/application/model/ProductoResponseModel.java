package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model;

import lombok.Data;

@Data
public class ProductoResponseModel {

    private Long id;

    private String nombre;

    private Long categoriaId;

    private Integer precio;

    private Boolean activo;
}
