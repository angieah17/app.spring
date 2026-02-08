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
public class JuegoPreguntaService {
	
	private final PreguntaRepository preguntaRepository;

    public JuegoPreguntaService(PreguntaRepository preguntaRepository) {
        this.preguntaRepository = preguntaRepository;
    }

    /**
     * Obtiene una pregunta activa aleatoria de la base de datos.
     * 
     * @return Una pregunta aleatoria que esté activa
     * @throws ResourceNotFoundException si no hay preguntas activas disponibles
     */
    public Pregunta obtenerPreguntaAleatoria() {
        long count = preguntaRepository.countByActivaTrue();
    
    if (count == 0) {
        throw new ResourceNotFoundException("No hay preguntas activas disponibles");
    }
    
    int randomOffset = (int) (Math.random() * count);
    Page<Pregunta> page = preguntaRepository.findByActivaTrue(
        PageRequest.of(randomOffset, 1)
    );
    
    return page.getContent().get(0);
    }
    

    /**
 * Obtiene preguntas activas por temática con paginación.
 * 
 * @param tematica La temática a buscar
 * @param pageable Configuración de paginación
 * @return Una página con las preguntas activas que coincidan con la temática
 * @throws TematicaInvalidaException si no existen preguntas activas con esa temática
 */
public Page<Pregunta> obtenerPreguntasActivasPorTematica(String tematica, Pageable pageable) {
    Page<Pregunta> resultado = preguntaRepository.findByTematicaAndActivaTrue(tematica.trim(), pageable);
    
    if (resultado.isEmpty()) {
        throw new TematicaInvalidaException("No existen preguntas activas para la temática: " + tematica);
    }
    
    return resultado;
}

}
