package com.midominio.group.app.spring.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.midominio.group.app.spring.entity.Pregunta;
import com.midominio.group.app.spring.exception.ResourceNotFoundException;
import com.midominio.group.app.spring.repository.PreguntaRepository;

import static com.midominio.group.app.spring.repository.PreguntaSpecifications.*;

/**
 * Servicio de búsqueda y gestión de preguntas para administración.
 * Permite combinar múltiples filtros de forma flexible (temática, tipo, estado).
 * 
 * A diferencia de TestService, este servicio:
 * - No filtra solo activas por defecto
 * - Permite ver tanto activas como inactivas
 * - Está enfocado en operaciones de administración (lectura, activación/desactivación)
 * 
 * NOTA: Para crear y editar preguntas, usar los servicios específicos por tipo
 * (PreguntaVFService, PreguntaUnicaService, PreguntaMultipleService) ya que
 * cada tipo tiene campos específicos diferentes (respuestas, opciones, etc).
 */
@Service
public class PreguntaSearchService {
    
    private final PreguntaRepository preguntaRepository;

    public PreguntaSearchService(PreguntaRepository preguntaRepository) {
        this.preguntaRepository = preguntaRepository;
    }

    /**
     * Busca preguntas combinando múltiples filtros opcionales.
     * Todos los parámetros son opcionales (null o vacío = no aplica ese filtro).
     * 
     * Filtros disponibles:
     * - tematica: filtra por temática exacta
     * - tipoPregunta: filtra por tipo (VERDADERO_FALSO, UNICA, MULTIPLE)
     * - activa: filtra por estado (true=activas, false=inactivas, null=todas)
     * 
     * @param tematica La temática a buscar (opcional)
     * @param tipoPregunta El tipo de pregunta (opcional)
     * @param activa El estado de la pregunta (opcional: true, false o null)
     * @param pageable Configuración de paginación y ordenamiento
     * @return Una página con las preguntas que cumplan los criterios
     */
    public Page<Pregunta> buscarConFiltros(
            String tematica, 
            String tipoPregunta, 
            Boolean activa, 
            Pageable pageable) {
        
        // Construir la especificación combinando filtros atómicos
        // Los filtros null o vacíos se ignoran automáticamente
        Specification<Pregunta> spec = Specification.where(conTematica(tematica))
                                                    .and(conTipoPregunta(tipoPregunta))
                                                    .and(conEstado(activa));
        
        return preguntaRepository.findAll(spec, pageable);
    }

    /**
     * Obtiene todas las preguntas sin filtros (activas e inactivas).
     * 
     * @param pageable Configuración de paginación y ordenamiento
     * @return Una página con todas las preguntas
     */
    public Page<Pregunta> obtenerTodas(Pageable pageable) {
        return preguntaRepository.findAll(pageable);
    }

    /**
     * Busca preguntas por texto en el enunciado, opcionalmente combinado con otros filtros.
     * Búsqueda case-insensitive parcial (contiene).
     * 
     * @param texto El texto a buscar en los enunciados (opcional)
     * @param tematica La temática a buscar (opcional)
     * @param tipoPregunta El tipo de pregunta (opcional)
     * @param activa El estado de la pregunta (opcional)
     * @param pageable Configuración de paginación y ordenamiento
     * @return Una página con las preguntas que cumplan los criterios
     */
    public Page<Pregunta> buscarPorTextoYFiltros(
            String texto,
            String tematica, 
            String tipoPregunta, 
            Boolean activa, 
            Pageable pageable) {
        
        Specification<Pregunta> spec = Specification.where(textoEnEnunciado(texto))
                                                    .and(conTematica(tematica))
                                                    .and(conTipoPregunta(tipoPregunta))
                                                    .and(conEstado(activa));
        
        return preguntaRepository.findAll(spec, pageable);
    }

    /**
     * Obtiene una pregunta por su ID.
     * 
     * @param id El ID de la pregunta
     * @return La pregunta encontrada
     * @throws ResourceNotFoundException si no existe la pregunta
     */
    public Pregunta obtenerPorId(Long id) {
        return preguntaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pregunta no encontrada con id: " + id));
    }

    /**
     * Activa una pregunta (soft delete inverso).
     * 
     * @param id El ID de la pregunta a activar
     * @return La pregunta activada
     * @throws ResourceNotFoundException si no existe la pregunta
     */
    public Pregunta activar(Long id) {
        Pregunta pregunta = obtenerPorId(id);
        pregunta.setActiva(true);
        return preguntaRepository.save(pregunta);
    }

    /**
     * Desactiva una pregunta (soft delete).
     * 
     * @param id El ID de la pregunta a desactivar
     * @return La pregunta desactivada
     * @throws ResourceNotFoundException si no existe la pregunta
     */
    public Pregunta desactivar(Long id) {
        Pregunta pregunta = obtenerPorId(id);
        pregunta.setActiva(false);
        return preguntaRepository.save(pregunta);
    }
}

