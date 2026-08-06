package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model;

import lombok.Data;

@Data
public class CategoriaResponseModel {

    private Long id;

    private String nombre;

    private String vendedor;
}
