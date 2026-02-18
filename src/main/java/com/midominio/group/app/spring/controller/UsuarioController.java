package com.midominio.group.app.spring.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.midominio.group.app.spring.dto.UsuarioCreateDTO;
import com.midominio.group.app.spring.dto.UsuarioDTO;
import com.midominio.group.app.spring.service.UsuarioService;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "Operaciones sobre usuarios (administración)")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar usuarios (paginado)")
    public Page<UsuarioDTO> list(Pageable pageable) {
        return usuarioService.list(pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener usuario por ID")
    public ResponseEntity<UsuarioDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.get(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear nuevo usuario")
    public ResponseEntity<UsuarioDTO> create(@Valid @RequestBody UsuarioCreateDTO dto) {
        UsuarioDTO created = usuarioService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar usuario")
    public ResponseEntity<UsuarioDTO> update(@PathVariable Long id, @Valid @RequestBody UsuarioCreateDTO dto) {
        return ResponseEntity.ok(usuarioService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar usuario")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
