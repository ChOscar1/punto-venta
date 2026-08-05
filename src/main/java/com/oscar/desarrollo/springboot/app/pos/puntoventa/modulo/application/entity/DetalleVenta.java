package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
@Table(name = "detalle_venta")
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venta_id")
    private Venta venta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id")
    private Venta producto;

    @NotNull
    private Integer cantidad;

    @NotNull
    private Integer precioUnitario;

    @NotNull
    private Integer subtotal;
}
