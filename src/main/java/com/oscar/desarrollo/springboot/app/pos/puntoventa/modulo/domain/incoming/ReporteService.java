package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.incoming;

import java.time.LocalDateTime;

public interface ReporteService {

    byte[] generarReporte(LocalDateTime fechaInicio, LocalDateTime fechaFin);
}
