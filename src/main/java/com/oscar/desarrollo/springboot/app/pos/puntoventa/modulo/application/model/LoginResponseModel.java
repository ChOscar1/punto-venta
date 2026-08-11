package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseModel {

    private String token;
    private String username;
    private String nombre;
}
