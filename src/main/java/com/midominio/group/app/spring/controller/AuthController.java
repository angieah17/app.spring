package com.midominio.group.app.spring.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.midominio.group.app.spring.entity.RoleEnum;
import com.midominio.group.app.spring.entity.Usuario;
import com.midominio.group.app.spring.repository.UsuarioRepository;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    
    public AuthController(UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {
this.usuarioRepository = usuarioRepository;
this.passwordEncoder = passwordEncoder;
}

    @PostMapping("/register")
    public Usuario register(@RequestBody Usuario usuario) {

        usuario.setPassword(
            passwordEncoder.encode(usuario.getPassword())
        );

        usuario.setRole(RoleEnum.USER);
        usuario.setEnabled(true);

        return usuarioRepository.save(usuario);
    }
}
