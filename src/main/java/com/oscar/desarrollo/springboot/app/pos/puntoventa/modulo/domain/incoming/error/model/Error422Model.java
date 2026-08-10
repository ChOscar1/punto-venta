package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.incoming.error.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@JsonPropertyOrder({
        "error", "message", "timestamp", "errores"
})
public class Error422Model {

    private String error;

    private String message;

    private LocalDateTime timestamp;

    private List<CampoErrorResponse> errores;
}
