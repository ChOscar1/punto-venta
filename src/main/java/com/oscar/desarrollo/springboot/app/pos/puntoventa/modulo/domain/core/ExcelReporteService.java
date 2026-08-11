package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.core;

import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.entity.DetalleVenta;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.entity.Venta;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExcelReporteService {

    public byte[] generarExcel(List<Venta> ventas) {

        try (Workbook workbook = new XSSFWorkbook()) {

            crearHojaDetalle(workbook, ventas);

            crearHojaResumen(workbook, ventas);

            ByteArrayOutputStream outputStream =
                    new ByteArrayOutputStream();

            workbook.write(outputStream);

            return outputStream.toByteArray();

        } catch (IOException e) {

            throw new RuntimeException(
                    "Error al generar el reporte de Excel", e);
        }
    }

    private void crearHojaDetalle(
            Workbook workbook,
            List<Venta> ventas) {

        Sheet sheet = workbook.createSheet("Detalle Ventas");

        Row encabezado = sheet.createRow(0);

        encabezado.createCell(0).setCellValue("ID Venta");
        encabezado.createCell(1).setCellValue("Fecha");
        encabezado.createCell(2).setCellValue("Vendedor");
        encabezado.createCell(3).setCellValue("Producto");
        encabezado.createCell(4).setCellValue("Categoría");
        encabezado.createCell(5).setCellValue("Cantidad");
        encabezado.createCell(6).setCellValue("Precio Unitario");
        encabezado.createCell(7).setCellValue("Subtotal");
        encabezado.createCell(8).setCellValue("Método Pago");

        int fila = 1;

        for (Venta venta : ventas) {

            for (DetalleVenta detalle : venta.getDetalles()) {

                Row row = sheet.createRow(fila++);

                row.createCell(0)
                        .setCellValue(venta.getId());

                row.createCell(1)
                        .setCellValue(venta.getFecha().toString());

                row.createCell(2)
                        .setCellValue(
                                venta.getVendedor().getNombre()
                        );

                row.createCell(3)
                        .setCellValue(
                                detalle.getProducto().getNombre()
                        );

                row.createCell(4)
                        .setCellValue(
                                detalle.getProducto()
                                        .getCategoria()
                                        .getNombre()
                        );

                row.createCell(5)
                        .setCellValue(
                                detalle.getCantidad()
                        );

                row.createCell(6)
                        .setCellValue(
                                detalle.getPrecioUnitario()
                        );

                row.createCell(7)
                        .setCellValue(
                                detalle.getSubtotal()
                        );

                row.createCell(8)
                        .setCellValue(
                                venta.getMetodoPago()
                        );
            }
        }

        for (int i = 0; i < 9; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void crearHojaResumen(
            Workbook workbook,
            List<Venta> ventas) {

        Sheet sheet = workbook.createSheet("Resumen");

        Map<String, Integer> totalPorVendedor =
                new HashMap<>();

        for (Venta venta : ventas) {

            String vendedor =
                    venta.getVendedor().getNombre();

            totalPorVendedor.merge(
                    vendedor,
                    venta.getTotal(),
                    Integer::sum
            );
        }

        Row encabezado = sheet.createRow(0);

        encabezado.createCell(0)
                .setCellValue("Vendedor");

        encabezado.createCell(1)
                .setCellValue("Total Vendido");

        int fila = 1;

        int totalGeneral = 0;

        for (Map.Entry<String, Integer> entry
                : totalPorVendedor.entrySet()) {

            Row row = sheet.createRow(fila++);

            row.createCell(0)
                    .setCellValue(entry.getKey());

            row.createCell(1)
                    .setCellValue(entry.getValue());

            totalGeneral += entry.getValue();
        }

        Row total = sheet.createRow(fila);

        total.createCell(0)
                .setCellValue("TOTAL GENERAL");

        total.createCell(1)
                .setCellValue(totalGeneral);

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
    }
}
