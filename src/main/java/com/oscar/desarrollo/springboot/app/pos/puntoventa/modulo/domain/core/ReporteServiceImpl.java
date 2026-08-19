package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.core;

import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.entity.Venta;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.incoming.ReporteService;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.incoming.error.exception.BussinessException;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.infraestructure.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReporteServiceImpl implements ReporteService {

    @Autowired
    VentaRepository ventaRepository;
    @Autowired
    ExcelReporteService excelReporteService;

    @Override
    public byte[] generarReporte(LocalDateTime inicio, LocalDateTime fin) {

        if (inicio == null || fin == null) {
            throw new BussinessException("La fecha y hora de inicio y fin son obligatorias");
        }

        if (inicio.isAfter(fin)) {
            throw new BussinessException("La fecha y hora de inicio no puede ser posterior a la fecha y hora de fin");
        }

        List<Venta> ventas = ventaRepository.findByPedidoEstadoAndFechaBetween("ENTREGADO", inicio, fin);

        return excelReporteService.generarExcel(ventas);
    }
}
