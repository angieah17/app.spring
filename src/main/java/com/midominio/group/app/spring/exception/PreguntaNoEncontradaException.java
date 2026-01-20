package com.midominio.group.app.spring.exception;

public class PreguntaNoEncontradaException extends RuntimeException {
	
	   public PreguntaNoEncontradaException(Long id) {
	        super("Pregunta no encontrada con id: " + id);
	    }
}
