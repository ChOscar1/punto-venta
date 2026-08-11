package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.infraestructure;

import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository <Categoria, Long> {

    List<Categoria> findByVendedorId(Long vendedorId);
}
