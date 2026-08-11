package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.core;

import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.entity.Usuario;
import org.springframework.security.core.userdetails.User;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.infraestructure.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No se encontró el usuario: " + username));

        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .authorities("USER")
                .disabled(!usuario.getActivo())
                .build();
    }
}
