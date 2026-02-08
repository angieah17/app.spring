package com.midominio.group.app.spring.dto;

import com.midominio.group.app.spring.entity.Pregunta;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO que representa un test generado con preguntas seleccionadas.
 */
public class Test {
    
    private String id;
    
    private List<Pregunta> preguntas;
    
    private Integer cantidad;
    
    private LocalDateTime fechaCreacion;
    
    public Test() {
        this.id = UUID.randomUUID().toString();
        this.fechaCreacion = LocalDateTime.now();
    }
    
    public Test(List<Pregunta> preguntas) {
        this();
        this.preguntas = preguntas;
        this.cantidad = preguntas != null ? preguntas.size() : 0;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public List<Pregunta> getPreguntas() {
        return preguntas;
    }
    
    public void setPreguntas(List<Pregunta> preguntas) {
        this.preguntas = preguntas;
        this.cantidad = preguntas != null ? preguntas.size() : 0;
    }
    
    public Integer getCantidad() {
        return cantidad;
    }
    
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
