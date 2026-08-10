package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.incoming.error.exception;


public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {

        super(message);
    }
}
