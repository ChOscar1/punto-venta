package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.core;

import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.entity.Venta;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.incoming.ReporteService;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.infraestructure.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReporteServiceImpl implements ReporteService {

    @Autowired
    VentaRepository ventaRepository;
    @Autowired
    ExcelReporteService excelReporteService;

    @Override
    public byte[] generarReporte(LocalDate fecha) {

        LocalDateTime inicio = fecha.atStartOfDay();

        LocalDateTime fin = fecha
                .plusDays(1)
                .atStartOfDay()
                .minusNanos(1);

        List<Venta> ventas = ventaRepository.findByFechaBetween(inicio, fin);

        return excelReporteService.generarExcel(ventas);
    }
}
