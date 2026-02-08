package com.midominio.group.app.spring.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.midominio.group.app.spring.entity.Pregunta;
import com.midominio.group.app.spring.exception.ResourceNotFoundException;
import com.midominio.group.app.spring.exception.TematicaInvalidaException;
import com.midominio.group.app.spring.repository.PreguntaRepository;

/* Operaciones sobre todas las preguntas */

@Service
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
    
    /**
     * Obtiene preguntas por temática con paginación.
     * 
     * @param tematica La temática a buscar
     * @param pageable Configuración de paginación
     * @return Una página con las preguntas que coincidan con la temática
     * @throws TematicaInvalidaException si no existen preguntas con esa temática
     */
    public Page<Pregunta> obtenerPreguntasPorTematica(String tematica, Pageable pageable) {
    	Page<Pregunta> resultado = preguntaRepository.findByTematica(tematica.trim(), pageable);
        
        if (resultado.isEmpty()) {
            throw new TematicaInvalidaException("No existen preguntas para la temática: " + tematica);
        }
        
        return resultado;
    }

}
