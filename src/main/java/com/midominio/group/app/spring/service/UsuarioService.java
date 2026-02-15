package com.midominio.group.app.spring.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.midominio.group.app.spring.dto.UsuarioAuthDTO;
import com.midominio.group.app.spring.entity.RoleEnum;
import com.midominio.group.app.spring.entity.Usuario;
import com.midominio.group.app.spring.exception.BadRequestException;
import com.midominio.group.app.spring.exception.ResourceNotFoundException;
import com.midominio.group.app.spring.repository.UsuarioRepository;

//Servicio para manejar la lógica de negocio relacionada con los usuarios,
//como el registro y la obtención del perfil autenticado.
//  Utiliza el repositorio para interactuar con la base de datos y el password encoder para hashear las contraseñas. 
// Devuelve DTOs para evitar exponer información sensible.

/*Diferente a UserDetailsImpl que tiene responsabilidad de seguridad: cargar usuario para que Spring Security autentique. */

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioAuthDTO register(Usuario usuario) {
        if (usuarioRepository.existsByUsername(usuario.getUsername())) {
            throw new BadRequestException("El nombre de usuario ya existe");
        }

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setRole(RoleEnum.USER);
        usuario.setEnabled(true);

        Usuario saved = usuarioRepository.save(usuario);
        return toDto(saved);
    }

    public UsuarioAuthDTO getAuthenticatedProfile(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        return toDto(usuario);
    }

    private UsuarioAuthDTO toDto(Usuario usuario) {
        return new UsuarioAuthDTO(
            usuario.getId(),
            usuario.getUsername(),
            usuario.getRole().name(),
            usuario.isEnabled()
        );
    }
}
