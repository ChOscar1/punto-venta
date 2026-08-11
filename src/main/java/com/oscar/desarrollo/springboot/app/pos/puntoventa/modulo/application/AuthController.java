package com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application;

import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.entity.Usuario;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.LoginModelRequest;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.application.model.LoginResponseModel;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.domain.core.JwtService;
import com.oscar.desarrollo.springboot.app.pos.puntoventa.modulo.infraestructure.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    JwtService jwtService;

    @PostMapping("/login")
    public LoginResponseModel login(@Valid @RequestBody LoginModelRequest request) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(), request.getPassword()));

        Usuario usuario = usuarioRepository.findByUsername(request.getUsername()).orElseThrow();

        String token = jwtService.generarToken((org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal());

        return new LoginResponseModel(token, usuario.getUsername(), usuario.getNombre());
    }
}
