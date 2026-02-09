package com.midominio.group.app.spring.dto;

/**
 * DTO que contiene la revisión detallada de una pregunta después del test.
 * Incluye la respuesta del usuario, la correcta y la explicación.
 */
public class PreguntaResultDTO {
    
    private Long preguntaId;
    
    private String enunciado;
    
    private String tipoPregunta;
    
    private Object respuestaUsuario;
    
    private Object respuestaCorrecta;
    
    private Boolean esCorrecta;
    
    private String explicacion;
    
    
    // CONSTRUCTORES
    
    public PreguntaResultDTO() {
    }
    
    public PreguntaResultDTO(Long preguntaId, String enunciado, String tipoPregunta,
                            Object respuestaUsuario, Object respuestaCorrecta, 
                            Boolean esCorrecta, String explicacion) {
        this.preguntaId = preguntaId;
        this.enunciado = enunciado;
        this.tipoPregunta = tipoPregunta;
        this.respuestaUsuario = respuestaUsuario;
        this.respuestaCorrecta = respuestaCorrecta;
        this.esCorrecta = esCorrecta;
        this.explicacion = explicacion;
    }
    
    
    // GETTERS Y SETTERS
    
    public Long getPreguntaId() {
        return preguntaId;
    }
    
    public void setPreguntaId(Long preguntaId) {
        this.preguntaId = preguntaId;
    }
    
    public String getEnunciado() {
        return enunciado;
    }
    
    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }
    
    public String getTipoPregunta() {
        return tipoPregunta;
    }
    
    public void setTipoPregunta(String tipoPregunta) {
        this.tipoPregunta = tipoPregunta;
    }
    
    public Object getRespuestaUsuario() {
        return respuestaUsuario;
    }
    
    public void setRespuestaUsuario(Object respuestaUsuario) {
        this.respuestaUsuario = respuestaUsuario;
    }
    
    public Object getRespuestaCorrecta() {
        return respuestaCorrecta;
    }
    
    public void setRespuestaCorrecta(Object respuestaCorrecta) {
        this.respuestaCorrecta = respuestaCorrecta;
    }
    
    public Boolean getEsCorrecta() {
        return esCorrecta;
    }
    
    public void setEsCorrecta(Boolean esCorrecta) {
        this.esCorrecta = esCorrecta;
    }
    
    public String getExplicacion() {
        return explicacion;
    }
    
    public void setExplicacion(String explicacion) {
        this.explicacion = explicacion;
    }
}
