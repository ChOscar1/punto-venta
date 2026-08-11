package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.infraestructure;

import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);
}
