package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "ventas")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDateTime fecha;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendedor_id")
    private Vendedor vendedor;

    @NotNull
    private Integer subtotal;

    @NotNull
    private Integer descuento;

    @NotNull
    private Integer total;

    @NotBlank
    private String metodoPago;

}
