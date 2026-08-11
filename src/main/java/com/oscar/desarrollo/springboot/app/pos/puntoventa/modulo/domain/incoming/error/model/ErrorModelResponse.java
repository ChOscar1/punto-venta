package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.incoming.error.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorModelResponse {

    private String error;

    private String message;

    private LocalDateTime timestamp;

}
