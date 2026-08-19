package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.core;

import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.entity.DetalleVenta;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.entity.Pedido;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.entity.Venta;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFTable;
import org.springframework.stereotype.Service;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    private void crearHojaDetalle(Workbook workbook, List<Venta> ventas) {

        Sheet sheet = workbook.createSheet("Detalle Ventas");

        Row encabezado = sheet.createRow(0);

        encabezado.createCell(0).setCellValue("ID Pedido");
        encabezado.createCell(1).setCellValue("ID Venta");
        encabezado.createCell(2).setCellValue("Fecha");
        encabezado.createCell(3).setCellValue("Cliente");
        encabezado.createCell(4).setCellValue("Vendedor");
        encabezado.createCell(5).setCellValue("Producto");
        encabezado.createCell(6).setCellValue("Categoría");
        encabezado.createCell(7).setCellValue("Cantidad");
        encabezado.createCell(8).setCellValue("Precio Unitario");
        encabezado.createCell(9).setCellValue("Subtotal Producto");
        encabezado.createCell(10).setCellValue("Descuento");
        encabezado.createCell(11).setCellValue("Total Venta");
        encabezado.createCell(12).setCellValue("Método Pago");
        encabezado.createCell(13).setCellValue("Estado Pedido");
        encabezado.createCell(14).setCellValue("Estado Pago");
        encabezado.createCell(15).setCellValue("Monto Pagado");
        encabezado.createCell(16).setCellValue("Monto Pendiente");

        int fila = 1;

        for (Venta venta : ventas) {

            Pedido pedido = venta.getPedido();

            for (DetalleVenta detalle : venta.getDetalles()) {

                Row row = sheet.createRow(fila++);

                row.createCell(0).setCellValue(
                        venta.getPedido().getId()
                );

                row.createCell(1).setCellValue(
                        venta.getId()
                );

                row.createCell(2).setCellValue(
                        venta.getFecha().toString()
                );

                row.createCell(3).setCellValue(
                        venta.getPedido().getNombreCliente()
                );

                row.createCell(4).setCellValue(
                        venta.getVendedor().getNombre()
                );

                row.createCell(5).setCellValue(
                        detalle.getProducto().getNombre()
                );

                row.createCell(6).setCellValue(
                        detalle.getProducto()
                                .getCategoria()
                                .getNombre()
                );

                row.createCell(7).setCellValue(
                        detalle.getCantidad()
                );

                row.createCell(8).setCellValue(
                        detalle.getPrecioUnitario()
                );

                row.createCell(9).setCellValue(
                        detalle.getSubtotal()
                );

                row.createCell(10).setCellValue(
                        venta.getDescuento()
                );

                row.createCell(11).setCellValue(
                        venta.getTotal()
                );

                row.createCell(12).setCellValue(
                        venta.getMetodoPago()
                );

                row.createCell(13).setCellValue(
                        venta.getPedido().getEstado()
                );

                int totalPedido = pedido.getVentas()
                        .stream()
                        .mapToInt(Venta::getTotal)
                        .sum();

                int montoPagado = pedido.getMontoPagado() != null ? pedido.getMontoPagado() : 0;

                int montoPendiente = totalPedido - montoPagado;

                row.createCell(14).setCellValue(
                        pedido.getEstadoPago()
                );

                row.createCell(15).setCellValue(
                        pedido.getMontoPagado()
                );

                row.createCell(16).setCellValue(montoPendiente);
            }
        }

        for (int i = 0; i <= 16; i++) {
            sheet.autoSizeColumn(i);
        }

        int ultimaFila = fila - 1;

        if (ultimaFila >= 1) {

            AreaReference area = workbook.getCreationHelper()
                    .createAreaReference(
                            new CellReference(0, 0),
                            new CellReference(ultimaFila, 16)
                    );

            XSSFTable tabla = ((XSSFSheet) sheet).createTable(area);

            tabla.setName("TablaDetalleVentas");
            tabla.setDisplayName("TablaDetalleVentas");

            tabla.getCTTable().addNewAutoFilter();
        }
    }

    private void crearHojaResumen(Workbook workbook, List<Venta> ventas) {

        Sheet sheet = workbook.createSheet("Resumen");

        Map<String, Integer> totalPorVendedor = new HashMap<>();

        for (Venta venta : ventas) {

            String vendedor = venta.getVendedor().getNombre();

            totalPorVendedor.merge(vendedor, venta.getTotal(), Integer::sum);
        }

        Row encabezado = sheet.createRow(0);

        encabezado.createCell(0).setCellValue("Vendedor");

        encabezado.createCell(1).setCellValue("Total Vendido");

        int fila = 1;

        int totalGeneral = 0;

        for (Map.Entry<String, Integer> entry : totalPorVendedor.entrySet()) {

            Row row = sheet.createRow(fila++);

            row.createCell(0).setCellValue(entry.getKey());

            row.createCell(1).setCellValue(entry.getValue());

            totalGeneral += entry.getValue();
        }

        Row total = sheet.createRow(fila);

        total.createCell(0).setCellValue("TOTAL GENERAL");

        total.createCell(1).setCellValue(totalGeneral);

        Set<Pedido> pedidos = ventas.stream()
                .map(Venta::getPedido)
                .collect(Collectors.toSet());

        int totalPagado = 0;
        int totalPendiente = 0;

        for (Pedido pedido : pedidos) {

            int montoPagado = pedido.getMontoPagado() != null
                    ? pedido.getMontoPagado()
                    : 0;

            int totalPedido = pedido.getVentas()
                    .stream()
                    .mapToInt(Venta::getTotal)
                    .sum();

            int montoPendiente = totalPedido - montoPagado;

            totalPagado += montoPagado;
            totalPendiente += montoPendiente;
        }

        int filaPagos = fila + 3;

        Row tituloPagos = sheet.createRow(filaPagos);

        tituloPagos.createCell(0)
                .setCellValue("RESUMEN DE PAGOS");


        Row vendidoRow = sheet.createRow(filaPagos + 1);

        vendidoRow.createCell(0)
                .setCellValue("Total Vendido");

        vendidoRow.createCell(1)
                .setCellValue(totalGeneral);


        Row pagadoRow = sheet.createRow(filaPagos + 2);

        pagadoRow.createCell(0)
                .setCellValue("Total Pagado");

        pagadoRow.createCell(1)
                .setCellValue(totalPagado);


        Row pendienteRow = sheet.createRow(filaPagos + 3);

        pendienteRow.createCell(0)
                .setCellValue("Total Pendiente");

        pendienteRow.createCell(1)
                .setCellValue(totalPendiente);

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
    }
}
