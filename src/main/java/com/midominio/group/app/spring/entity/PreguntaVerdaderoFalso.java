package com.midominio.group.app.spring.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "preguntas_verdadero_falso")
@DiscriminatorValue("VERDADERO_FALSO")
public class PreguntaVerdaderoFalso extends Pregunta {
    
	//1. ATRIBUTOS
    @Column(name = "respuesta_correcta", nullable = false) //este campo no puede estar vacío
    private Boolean respuestaCorrecta;
    
    @Column(length = 1000)
    private String explicacion;
    
    
    //CONSTRUCTORES
    
    public PreguntaVerdaderoFalso() {
    	
    }
    
    public PreguntaVerdaderoFalso(Boolean respuestaCorrecta, String explicacion) {
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