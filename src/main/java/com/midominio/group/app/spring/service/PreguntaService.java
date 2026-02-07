package com.midominio.group.app.spring.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.midominio.group.app.spring.entity.Pregunta;
import com.midominio.group.app.spring.exception.ResourceNotFoundException;
import com.midominio.group.app.spring.repository.PreguntaRepository;

/* Operaciones sobre todas las preguntas */

public class PreguntaService {
	
	private final PreguntaRepository preguntaRepository;

    public PreguntaService(PreguntaRepository preguntaRepository) {
        this.preguntaRepository = preguntaRepository;
    }

    /**
     * Obtiene una pregunta activa aleatoria de la base de datos.
     * 
     * @return Una pregunta aleatoria que esté activa
     * @throws ResourceNotFoundException si no hay preguntas activas disponibles
     */
    public Pregunta obtenerPreguntaAleatoria() {
        Page<Pregunta> page = preguntaRepository.findRandomActiva(PageRequest.of(0, 1));
        
        if (page.isEmpty()) {
            throw new ResourceNotFoundException("No hay preguntas activas disponibles");
        }
        
        return page.getContent().get(0);
    }

}
