package com.midominio.group.app.spring.dto;

import java.util.List;

/**
 * DTO que representa el resultado completo de un test con revisión y explicaciones.
 */
public class TestResultDTO {
    
    private Integer puntuacion; // 0 a 100
    
    private Integer preguntasCorrectas;
    
    private Integer cantidadPreguntas;
    
    private String tematica;
    
    private List<PreguntaResultDTO> revision; // Revisión detallada de cada pregunta
    
    
    // CONSTRUCTORES
    
    public TestResultDTO() {
    }
    
    public TestResultDTO(Integer puntuacion, Integer preguntasCorrectas, 
                        Integer cantidadPreguntas, String tematica, List<PreguntaResultDTO> revision) {
        this.puntuacion = puntuacion;
        this.preguntasCorrectas = preguntasCorrectas;
        this.cantidadPreguntas = cantidadPreguntas;
        this.tematica = tematica;
        this.revision = revision;
    }
    
    
    // GETTERS Y SETTERS
    
    public Integer getPuntuacion() {
        return puntuacion;
    }
    
    public void setPuntuacion(Integer puntuacion) {
        this.puntuacion = puntuacion;
    }
    
    public Integer getPreguntasCorrectas() {
        return preguntasCorrectas;
    }
    
    public void setPreguntasCorrectas(Integer preguntasCorrectas) {
        this.preguntasCorrectas = preguntasCorrectas;
    }
    
    public Integer getCantidadPreguntas() {
        return cantidadPreguntas;
    }
    
    public void setCantidadPreguntas(Integer cantidadPreguntas) {
        this.cantidadPreguntas = cantidadPreguntas;
    }
    
    public String getTematica() {
        return tematica;
    }
    
    public void setTematica(String tematica) {
        this.tematica = tematica;
    }
    
    public List<PreguntaResultDTO> getRevision() {
        return revision;
    }
    
    public void setRevision(List<PreguntaResultDTO> revision) {
        this.revision = revision;
    }
}
