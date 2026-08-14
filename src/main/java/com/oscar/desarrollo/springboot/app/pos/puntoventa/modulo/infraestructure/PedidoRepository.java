package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.infraestructure;

import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByEstado(String estado);
}