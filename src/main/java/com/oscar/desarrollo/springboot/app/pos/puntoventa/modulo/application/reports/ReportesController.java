package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.reports;

import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.incoming.ReporteService;

import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/punto-venta/reporte")
public class ReportesController {

    @Autowired
    ReporteService reporteService;

    @GetMapping("/ventas/excel")
    public ResponseEntity<byte[]> generarReporte(@RequestParam LocalDate fecha) {

        byte[] archivo = reporteService.generarReporte(fecha);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=reporte-ventas-" + fecha + ".xlsx"
                )
                .contentType(
                        MediaType.parseMediaType(
                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                        )
                )
                .body(archivo);
    }
}
