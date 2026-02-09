package com.midominio.group.app.spring.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * Entidad que representa el resultado de un test realizado por un usuario.
 * Solo persiste el resumen del resultado, no cada respuesta individual.
 */
@Entity
@Table(name = "resultados_test")
public class ResultadoTest {
    
    // 1. ATRIBUTOS
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "El usuario es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY) //Muchas entidades de un tipo se relacionan con una sola entidad de otro. En este caso, muchos resultados de test pueden estar asociados a un solo usuario.
    //fetch = FetchType.LAZY forzamos a cargar solo el ID del objeto relacionado hasta que se necesite.
    @JoinColumn(name = "usuario_id", nullable = false) //aquí crea la FK en la tabla resultados_test
    private Usuario usuario;
    
    @NotNull(message = "La fecha de realización es obligatoria")
    @Column(name = "fecha_realizacion", nullable = false)
    private LocalDateTime fechaRealizacion;
    
    @NotNull(message = "La puntuación es obligatoria")
    @Column(name = "puntuacion", nullable = false)
    private Integer puntuacion; // Puntuación del 0 al 100
    
    @NotNull(message = "La temática es obligatoria")
    @Column(name = "tematica", nullable = false, length = 100)
    private String tematica;
    
    @Column(name = "cantidad_preguntas")
    private Integer cantidadPreguntas;
    
    @Column(name = "preguntas_correctas")
    private Integer preguntasCorrectas;
    
    
    // 2. CONSTRUCTORES
    
    public ResultadoTest() {
        this.fechaRealizacion = LocalDateTime.now();
    }
    
    public ResultadoTest(Usuario usuario, String tematica, Integer puntuacion, 
                        Integer cantidadPreguntas, Integer preguntasCorrectas) {
        this.usuario = usuario;
        this.tematica = tematica;
        this.puntuacion = puntuacion;
        this.cantidadPreguntas = cantidadPreguntas;
        this.preguntasCorrectas = preguntasCorrectas;
        this.fechaRealizacion = LocalDateTime.now();
    }
    
    
    // 3. GETTERS Y SETTERS
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public LocalDateTime getFechaRealizacion() {
        return fechaRealizacion;
    }
    
    public void setFechaRealizacion(LocalDateTime fechaRealizacion) {
        this.fechaRealizacion = fechaRealizacion;
    }
    
    public Integer getPuntuacion() {
        return puntuacion;
    }
    
    public void setPuntuacion(Integer puntuacion) {
        this.puntuacion = puntuacion;
    }
    
    public String getTematica() {
        return tematica;
    }
    
    public void setTematica(String tematica) {
        this.tematica = tematica;
    }
    
    public Integer getCantidadPreguntas() {
        return cantidadPreguntas;
    }
    
    public void setCantidadPreguntas(Integer cantidadPreguntas) {
        this.cantidadPreguntas = cantidadPreguntas;
    }
    
    public Integer getPreguntasCorrectas() {
        return preguntasCorrectas;
    }
    
    public void setPreguntasCorrectas(Integer preguntasCorrectas) {
        this.preguntasCorrectas = preguntasCorrectas;
    }
}
