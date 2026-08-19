package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDateTime fecha;

    @NotBlank
    private String nombreCliente;

    @NotBlank
    private String estado;

    @NotNull
    private Integer montoPagado;

    @NotBlank
    private String estadoPago;

    @OneToMany(mappedBy = "pedido")
    private List<Venta> ventas = new ArrayList<>();
}