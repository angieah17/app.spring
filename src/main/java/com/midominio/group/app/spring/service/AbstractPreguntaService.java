package com.midominio.group.app.spring.service;

import com.midominio.group.app.spring.exception.DatosInvalidosException;
import com.midominio.group.app.spring.exception.RecursoNoEncontradoException;
import com.midominio.group.app.spring.model.Pregunta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Clase abstracta base para servicios específicos de preguntas.
 * Proporciona funcionalidades comunes reutilizables por todos los tipos de preguntas.
 * 
 * Estructura de herencia:
 * - AbstractPreguntaService<T> (esta clase)
 *   ├── PreguntaVerdaderoFalsoService extends AbstractPreguntaService<PreguntaVerdaderoFalso>
 *   ├── PreguntaSeleccionUnicaService extends AbstractPreguntaService<PreguntaSeleccionUnica>
 *   └── PreguntaSeleccionMultipleService extends AbstractPreguntaService<PreguntaSeleccionMultiple>
 * 
 * Usado en:
 * - Servicios específicos de cada tipo de pregunta
 * - Controllers CRUD de tipos específicos
 * - Validaciones comunes entre tipos
 * 
 * @param <T> tipo de pregunta (debe extender Pregunta)
 */
@Transactional
public abstract class AbstractPreguntaService<T extends Pregunta> {
    
    // Constantes de validación
    protected static final int ENUNCIADO_MIN_LENGTH = 10;
    protected static final int ENUNCIADO_MAX_LENGTH = 500;
    protected static final int TEMATICA_MAX_LENGTH = 100;
    protected static final int EXPLICACION_MAX_LENGTH = 1000;
    
    /**
     * Obtiene el repositorio específico para este tipo de pregunta.
     * Implementado por subclases.
     * 
     * @return repositorio JPA del tipo de pregunta
     */
    protected abstract JpaRepository<T, Long> getRepository();
    
    /**
     * Obtiene una pregunta por ID con validación
     * 
     * @param id identificador de la pregunta
     * @return la pregunta encontrada
     * @throws RecursoNoEncontradoException si no existe
     * @throws DatosInvalidosException si el ID es inválido
     */
    public T obtenerPorId(Long id) {
        validarIdPositivo(id);
        String nombreTipo = getRepository().getClass().getSimpleName();
        
        return getRepository().findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException(nombreTipo, id));
    }
    
    /**
     * Valida que un ID sea positivo (no nulo y mayor a 0)
     * Usado en: obtenerPorId(), actualizar(), validarRespuesta()
     * 
     * @param id identificador a validar
     * @throws DatosInvalidosException si el ID es inválido
     */
    protected void validarIdPositivo(Long id) {
        if (id == null || id <= 0) {
            throw new DatosInvalidosException("id", "El ID debe ser un número positivo");
        }
    }
    
    /**
     * Valida que un objeto no sea nulo
     * 
     * @param objeto objeto a validar
     * @param nombreObjeto nombre del objeto (para mensaje de error)
     * @throws DatosInvalidosException si el objeto es nulo
     */
    protected void validarNoNulo(Object objeto, String nombreObjeto) {
        if (objeto == null) {
            throw new DatosInvalidosException(
                nombreObjeto.toLowerCase(), 
                "El " + nombreObjeto + " no puede ser nulo"
            );
        }
    }
    
    /**
     * Valida el enunciado de una pregunta
     * Verifica: no nulo, no vacío, rango de caracteres permitido
     * 
     * @param enunciado enunciado a validar
     * @throws DatosInvalidosException si el enunciado es inválido
     */
    protected void validarEnunciado(String enunciado) {
        if (enunciado == null || enunciado.trim().isEmpty()) {
            throw new DatosInvalidosException("enunciado", "El enunciado es obligatorio");
        }
        
        int length = enunciado.trim().length();
        if (length < ENUNCIADO_MIN_LENGTH) {
            throw new DatosInvalidosException(
                "enunciado", 
                "El enunciado debe tener al menos " + ENUNCIADO_MIN_LENGTH + " caracteres"
            );
        }
        
        if (enunciado.length() > ENUNCIADO_MAX_LENGTH) {
            throw new DatosInvalidosException(
                "enunciado",
                "El enunciado no puede exceder " + ENUNCIADO_MAX_LENGTH + " caracteres"
            );
        }
    }
    
    /**
     * Valida la temática de una pregunta
     * Verifica: no nula, no vacía, longitud máxima permitida
     * 
     * @param tematica temática a validar
     * @throws DatosInvalidosException si la temática es inválida
     */
    protected void validarTematica(String tematica) {
        if (tematica == null || tematica.trim().isEmpty()) {
            throw new DatosInvalidosException("tematica", "La temática es obligatoria");
        }
        
        if (tematica.length() > TEMATICA_MAX_LENGTH) {
            throw new DatosInvalidosException(
                "tematica",
                "La temática no puede exceder " + TEMATICA_MAX_LENGTH + " caracteres"
            );
        }
    }
    
    /**
     * Valida la explicación de una pregunta (campo opcional)
     * Verifica: si existe, tiene longitud máxima permitida
     * 
     * @param explicacion explicación a validar
     * @throws DatosInvalidosException si la explicación es inválida
     */
    protected void validarExplicacion(String explicacion) {
        if (explicacion != null && !explicacion.trim().isEmpty()) {
            if (explicacion.length() > EXPLICACION_MAX_LENGTH) {
                throw new DatosInvalidosException(
                    "explicacion",
                    "La explicación no puede exceder " + EXPLICACION_MAX_LENGTH + " caracteres"
                );
            }
        }
    }
    
    /**
     * Valida que una respuesta del usuario no sea nula
     * 
     * @param respuesta respuesta a validar
     * @throws DatosInvalidosException si la respuesta es nula
     */
    protected void validarRespuestaUsuario(Boolean respuesta) {
        if (respuesta == null) {
            throw new DatosInvalidosException("respuesta", "La respuesta no puede ser nula");
        }
    }
    
    /**
     * Valida que una pregunta esté activa
     * 
     * @param pregunta pregunta a validar
     * @param idPregunta ID de la pregunta (para mensaje de error)
     * @throws IllegalStateException si la pregunta está inactiva
     */
    protected void validarPreguntaActiva(T pregunta, Long idPregunta) {
        if (!pregunta.getActiva()) {
            throw new IllegalStateException(
                "La pregunta con ID " + idPregunta + " está inactiva y no puede ser respondida"
            );
        }
    }
}
