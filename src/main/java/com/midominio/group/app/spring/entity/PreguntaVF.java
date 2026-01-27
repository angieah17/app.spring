package com.midominio.group.app.spring.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "preguntas_verdadero_falso") //tendrá su propia tabla en la BD gracias al InheritanceType.JOINED de la clase padre
@DiscriminatorValue("VERDADERO_FALSO")
public class PreguntaVF extends Pregunta {
    
	//1. ATRIBUTOS
	@NotNull(message = "La respuesta es obligatoria")
    @Column(name = "respuesta_correcta", nullable = false) //este campo no puede estar vacío
    private Boolean respuestaCorrecta; //Boolean más consistente con JPA que boolean, trabaja mejor con objetos
    
    @Column(length = 1000)
    private String explicacion;
    
    
    //CONSTRUCTORES
    
    public PreguntaVF() {
    	
    }
    
    public PreguntaVF(Boolean respuestaCorrecta, String explicacion) {
		this.respuestaCorrecta = respuestaCorrecta;
		this.explicacion = explicacion;
	}
    
    //METODOS
    
    @Override
    public String getTipoPregunta() {
        return "VERDADERO_FALSO";
    }
    
    public boolean validarRespuesta(Boolean respuesta) {
        return respuesta != null && respuesta.equals(this.respuestaCorrecta);
    }


	//GETTERS Y SETTERS
	public Boolean getRespuestaCorrecta() {
		return respuestaCorrecta;
	}

	public void setRespuestaCorrecta(Boolean respuestaCorrecta) {
		this.respuestaCorrecta = respuestaCorrecta;
	}

	public String getExplicacion() {
		return explicacion;
	}

	public void setExplicacion(String explicacion) {
		this.explicacion = explicacion;
	}
    
    
}