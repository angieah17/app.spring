package com.midominio.group.app.spring.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO que representa un test listo para ser respondido por el usuario.
 * Contiene las preguntas SIN las respuestas correctas.
 */
public class TestPlayDTO {
    
    private String testId;
    
    private String tematica;
    
    private LocalDateTime fechaCreacion;
    
    private List<PreguntaPlayDTO> preguntas;
    
    
    // CONSTRUCTORES
    
    public TestPlayDTO() {
    }
    
    public TestPlayDTO(String testId, String tematica, List<PreguntaPlayDTO> preguntas) {
        this.testId = testId;
        this.tematica = tematica;
        this.preguntas = preguntas;
        this.fechaCreacion = LocalDateTime.now();
    }
    
    
    // GETTERS Y SETTERS
    
    public String getTestId() {
        return testId;
    }
    
    public void setTestId(String testId) {
        this.testId = testId;
    }
    
    public String getTematica() {
        return tematica;
    }
    
    public void setTematica(String tematica) {
        this.tematica = tematica;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public List<PreguntaPlayDTO> getPreguntas() {
        return preguntas;
    }
    
    public void setPreguntas(List<PreguntaPlayDTO> preguntas) {
        this.preguntas = preguntas;
    }
}
