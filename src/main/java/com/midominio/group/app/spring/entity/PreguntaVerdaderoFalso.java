package com.midominio.group.app.spring.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "preguntas_verdadero_falso")
@DiscriminatorValue("VERDADERO_FALSO")
public class PreguntaVerdaderoFalso extends Pregunta {
    
    @Column(name = "respuesta_correcta", nullable = false) //este campo no puede estar vacío
    private Boolean respuestaCorrecta;
    
    @Column(length = 1000)
    private String explicacion;
    
    @Override
    public String getTipoPregunta() {
        return "VERDADERO_FALSO";
    }
    
    public boolean validarRespuesta(Boolean respuesta) {
        return respuesta != null && respuesta.equals(this.respuestaCorrecta);
    }
}