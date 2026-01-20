package com.midominio.group.app.spring.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.midominio.group.app.spring.entity.PreguntaVerdaderoFalso;
import com.midominio.group.app.spring.repository.PreguntaVerdaderoFalsoRepository;

import java.util.List;

/**
 * Service específico para preguntas de tipo Verdadero/Falso.
 * Solo contiene métodos que son exclusivos de este tipo de pregunta.
 * 
 * Para operaciones comunes (listar, filtrar, eliminar, etc.) usar PreguntaService (genérico).
 * 
 * Usado en:
 * - Controllers REST para crear/actualizar preguntas V/F
 * - Validación de respuestas en tests
 * - Generación de tests aleatorios solo con preguntas V/F
 */
@Service
@Transactional
public class PreguntaVerdaderoFalsoService {

    @Autowired
    private PreguntaVerdaderoFalsoRepository repository;

    /**
     * Crea una nueva pregunta de tipo Verdadero/Falso
     * Usado en: API REST POST /api/preguntas/verdadero-falso
     */
    public PreguntaVerdaderoFalso crear(PreguntaVerdaderoFalso pregunta) {
        // Asegura que las nuevas preguntas estén activas por defecto
        if (pregunta.getActiva() == null) {
            pregunta.setActiva(true);
        }
        return repository.save(pregunta);
    }

    /**
     * Actualiza una pregunta de tipo Verdadero/Falso existente
     * Usado en: API REST PUT /api/preguntas/verdadero-falso/{id}
     */
    public PreguntaVerdaderoFalso actualizar(Long id, PreguntaVerdaderoFalso preguntaActualizada) {
        
    	
    	
    	return repository.findById(id)
            .map(preguntaExistente -> {
                // Actualizar campos comunes (heredados de Pregunta)
                preguntaExistente.setEnunciado(preguntaActualizada.getEnunciado());
                preguntaExistente.setTematica(preguntaActualizada.getTematica());
                preguntaExistente.setActiva(preguntaActualizada.getActiva());
                
                // Actualizar campos específicos de Verdadero/Falso
                preguntaExistente.setRespuestaCorrecta(preguntaActualizada.getRespuestaCorrecta());
                preguntaExistente.setExplicacion(preguntaActualizada.getExplicacion());
                
                return repository.save(preguntaExistente);
            })
            .orElseThrow(() -> new RuntimeException("Pregunta no encontrada con id: " + id));
    }

    /**
     * Valida si la respuesta de un usuario a una pregunta V/F es correcta
     * Usado en: Procesamiento de respuestas en tests/evaluaciones
     * 
     * @param idPregunta ID de la pregunta
     * @param respuestaUsuario respuesta del usuario (true/false)
     * @return true si la respuesta es correcta
     */
    public boolean validarRespuesta(Long idPregunta, Boolean respuestaUsuario) {
        return repository.findById(idPregunta)
            .map(pregunta -> pregunta.validarRespuesta(respuestaUsuario))
            .orElseThrow(() -> new RuntimeException("Pregunta no encontrada con id: " + idPregunta));
    }

    /**
     * Obtiene preguntas aleatorias de tipo Verdadero/Falso para generar un test
     * Usado en: Generación automática de tests solo con preguntas V/F
     * 
     * @param cantidad número de preguntas a obtener
     * @return lista de preguntas aleatorias
     */
    public List<PreguntaVerdaderoFalso> obtenerPreguntasAleatorias(int cantidad) {
        Pageable pageable = PageRequest.of(0, cantidad);
        return repository.findRandomPreguntas(pageable);
    }
}
