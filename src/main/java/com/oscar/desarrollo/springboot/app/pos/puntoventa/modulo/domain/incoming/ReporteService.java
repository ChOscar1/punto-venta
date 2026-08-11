package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.incoming;

import java.time.LocalDate;

public interface ReporteService {

    byte[] generarReporte(LocalDate fecha);
}
