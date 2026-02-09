package com.midominio.group.app.spring.dto;

import java.util.Map;

/**
 * DTO que recibe las respuestas del usuario para un test.
 * Mapea preguntaId -> respuesta del usuario.
 */
public class TestSubmitDTO {
    
    private String testId;
    
    private String tematica;
    
    /**
     * Map donde:
     * - key: ID de la pregunta
     * - value: respuesta del usuario (puede ser boolean, integer, o lista de integers)
     */
    private Map<Long, Object> respuestas;
    
    
    // CONSTRUCTORES
    
    public TestSubmitDTO() {
    }
    
    public TestSubmitDTO(String testId, String tematica, Map<Long, Object> respuestas) {
        this.testId = testId;
        this.tematica = tematica;
        this.respuestas = respuestas;
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
    
    public Map<Long, Object> getRespuestas() {
        return respuestas;
    }
    
    public void setRespuestas(Map<Long, Object> respuestas) {
        this.respuestas = respuestas;
    }
}
