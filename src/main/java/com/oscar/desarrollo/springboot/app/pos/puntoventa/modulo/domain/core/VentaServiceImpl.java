package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.core;

import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.entity.DetalleVenta;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.entity.Producto;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.entity.Vendedor;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.entity.Venta;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.mapper.VentaMapper;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.DetalleVentaModelRequest;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.VentaModelRequest;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.VentaModelResponse;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.incoming.VentaService;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.incoming.error.exception.BussinessException;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.incoming.error.exception.ResourceNotFoundException;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.infraestructure.DetalleVentaRepository;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.infraestructure.ProductoRepository;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.infraestructure.VendedorRepository;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.infraestructure.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class VentaServiceImpl implements VentaService {

    @Autowired
    VentaRepository ventaRepository;
    @Autowired
    DetalleVentaRepository detalleRepository;
    @Autowired
    ProductoRepository prodRepository;
    @Autowired
    VendedorRepository vendedorRepository;
    @Autowired
    VentaMapper ventaMapper;

    @Override
    @Transactional
    public List<VentaModelResponse> registrarVenta(VentaModelRequest request) {

        Map<Long, Vendedor> vendedores = new LinkedHashMap<>();

        Map<Long, List<DetalleVenta>> detallesPorVendedor = new LinkedHashMap<>();


        for (DetalleVentaModelRequest item : request.getProductos()) {

            Producto producto = obtenerProducto(item.getProductoId());

            validarProducto(producto);

            Vendedor vendedor = producto.getCategoria().getVendedor();

            vendedores.put(vendedor.getId(), vendedor);


            int subtotalProducto = producto.getPrecio() * item.getCantidad();


            DetalleVenta detalle = ventaMapper.detalleVentaMapper(producto, item, subtotalProducto);


            detallesPorVendedor.computeIfAbsent(vendedor.getId(), key -> new ArrayList<>())
                    .add(detalle);
        }


        List<VentaModelResponse> respuestas = new ArrayList<>();


        for (Map.Entry<Long, List<DetalleVenta>> entry : detallesPorVendedor.entrySet()) {

            Vendedor vendedor = vendedores.get(entry.getKey());

            List<DetalleVenta> detalles = entry.getValue();

            int subtotalVenta = calcularSubtotal(detalles);

            Venta venta = ventaMapper.mapearVenta(request, vendedor, subtotalVenta);

            Venta ventaGuardada = ventaRepository.save(venta);


            guardarDetalles(detalles, ventaGuardada);

            ventaGuardada.setDetalles(detalles);

            respuestas.add(ventaMapper.responseModel(ventaGuardada));
        }

        return respuestas;
    }

    @Override
    @Transactional(readOnly = true)
    public VentaModelResponse buscarById(Long id) {

        Venta venta = ventaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No se encontró la venta con el id: " + id));

        return ventaMapper.responseModel(venta);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VentaModelResponse> listar() {

        return ventaRepository.findAll()
                .stream()
                .map(ventaMapper::responseModel)
                .toList();
    }

    private Producto obtenerProducto(Long productoId) {

        return prodRepository.findById(productoId).orElseThrow(() -> new ResourceNotFoundException("No se encontró el producto con el id: " + productoId));
    }

    private void validarProducto(Producto producto) {

        if (!producto.getActivo()) {
            throw new BussinessException("El producto no está activo: " + producto.getNombre());
        }
    }

    private int calcularSubtotal(List<DetalleVenta> detalles) {

        return detalles.stream()
                .mapToInt(DetalleVenta::getSubtotal)
                .sum();
    }

    private void guardarDetalles(List<DetalleVenta> detalles, Venta venta) {

        for (DetalleVenta detalle : detalles) {
            detalle.setVenta(venta);
        }

        detalleRepository.saveAll(detalles);
    }
}

