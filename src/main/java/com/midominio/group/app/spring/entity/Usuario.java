package com.midominio.group.app.spring.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Entidad que representa un usuario del sistema.
 * Contiene información de autenticación y autorización.
 */
@Entity
@Table(name = "usuarios")
public class Usuario {
    
    // 1. ATRIBUTOS
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    @NotNull(message = "El nombre de usuario es obligatorio")
    @Column(nullable = false, unique = true, length = 100)
    private String user;
    
    @NotBlank(message = "La contraseña no puede estar vacía")
    @NotNull(message = "La contraseña es obligatoria")
    @Column(nullable = false, length = 255)
    private String password;
    
    @NotBlank(message = "El rol no puede estar vacío")
    @NotNull(message = "El rol es obligatorio")
    @Column(nullable = false, length = 20)
    private String role; // "user" o "admin"
    
    @Column(columnDefinition = "boolean default true")
    private Boolean active = true;
    
    
    // 2. CONSTRUCTORES
    
    public Usuario() {
    }
    
    public Usuario(String user, String password, String role) {
        this.user = user;
        this.password = password;
        this.role = role;
        this.active = true;
    }
    
    
    // 3. GETTERS Y SETTERS
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUser() {
        return user;
    }
    
    public void setUser(String user) {
        this.user = user;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public Boolean getActive() {
        return active;
    }
    
    public void setActive(Boolean active) {
        this.active = active;
    }
}
