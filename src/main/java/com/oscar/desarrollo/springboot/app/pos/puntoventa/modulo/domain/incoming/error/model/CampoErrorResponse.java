package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.incoming.error.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CampoErrorResponse {

    private String campo;

    private String mensaje;
}
