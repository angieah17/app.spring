package com.midominio.group.app.spring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.midominio.group.app.spring.dto.UsuarioAuthDTO;
import com.midominio.group.app.spring.entity.Usuario;
import com.midominio.group.app.spring.service.UsuarioService;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticación", description = "Endpoints para registro y perfil del usuario")
public class AuthController {

    private final UsuarioService usuarioService;
    
    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar usuario")
    public ResponseEntity<UsuarioAuthDTO> register(@RequestBody Usuario usuario) {
        UsuarioAuthDTO response = usuarioService.register(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/me")
    @Operation(summary = "Perfil del usuario autenticado")
    public ResponseEntity<UsuarioAuthDTO> me(Authentication authentication) {
        UsuarioAuthDTO response = usuarioService.getAuthenticatedProfile(authentication.getName());
        return ResponseEntity.ok(response);
    }
}
