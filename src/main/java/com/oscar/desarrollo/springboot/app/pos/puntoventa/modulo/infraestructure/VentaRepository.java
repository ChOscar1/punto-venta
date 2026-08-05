package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.infraestructure;

import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
}
