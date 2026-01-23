package com.midominio.group.app.spring.service;


import com.midominio.group.app.spring.exception.RecursoNoEncontradoException;
import com.midominio.group.app.spring.entity.PreguntaVerdaderoFalso;
import com.midominio.group.app.spring.exception.DatosInvalidosException;
import com.midominio.group.app.spring.repository.PreguntaVerdaderoFalsoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class PreguntaVerdaderoFalsoService {
    
    @Autowired
    private PreguntaVerdaderoFalsoRepository repository;
    

    public PreguntaVerdaderoFalso crear(PreguntaVerdaderoFalso pregunta) {
        // Validación preventiva de datos de entrada
        // Lanza DatosInvalidosException si hay errores
        validarDatosPregunta(pregunta);
        
        // Asegura que las nuevas preguntas estén activas por defecto
        if (pregunta.getActiva() == null) {
            pregunta.setActiva(true);
        }
        
        return repository.save(pregunta);
    }
    

    public PreguntaVerdaderoFalso actualizar(Long id, PreguntaVerdaderoFalso preguntaActualizada) {
        // Validación preventiva: verificar que el ID sea válido
        if (id == null || id <= 0) {
            throw new DatosInvalidosException("id", "El ID debe ser un número positivo");
        }
        
        // Validación preventiva de datos de entrada
        // Lanza DatosInvalidosException si hay errores
        validarDatosPregunta(preguntaActualizada);
        
        // Buscar la pregunta existente con validación preventiva
        // Lanza RecursoNoEncontradoException si no existe
        PreguntaVerdaderoFalso preguntaExistente = repository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "Pregunta Verdadero/Falso", 
                id,
                "No se puede actualizar: pregunta no encontrada con ID: " + id
            ));
        
        // Actualizar campos comunes (heredados de Pregunta)
        preguntaExistente.setEnunciado(preguntaActualizada.getEnunciado());
        preguntaExistente.setTematica(preguntaActualizada.getTematica());
        preguntaExistente.setActiva(preguntaActualizada.getActiva());
        
        // Actualizar campos específicos de Verdadero/Falso
        preguntaExistente.setRespuestaCorrecta(preguntaActualizada.getRespuestaCorrecta());
        preguntaExistente.setExplicacion(preguntaActualizada.getExplicacion());
        
        return repository.save(preguntaExistente);
    }
    

     
    public boolean validarRespuesta(Long idPregunta, Boolean respuestaUsuario) {
        // Validación preventiva de parámetros de entrada
        if (idPregunta == null || idPregunta <= 0) {
            throw new DatosInvalidosException("idPregunta", "El ID de la pregunta debe ser un número positivo");
        }
        
        if (respuestaUsuario == null) {
            throw new DatosInvalidosException("respuestaUsuario", "La respuesta del usuario no puede ser nula");
        }
        
        // Buscar pregunta con validación preventiva
        // Lanza RecursoNoEncontradoException si no existe
        PreguntaVerdaderoFalso pregunta = repository.findById(idPregunta)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "Pregunta Verdadero/Falso",
                idPregunta,
                "No se puede validar respuesta: pregunta no encontrada con ID: " + idPregunta
            ));
        
        // Validar que la pregunta esté activa antes de permitir su uso
        // Lanza IllegalStateException (manejado como HTTP 409 en GlobalExceptionHandler)
        if (!pregunta.getActiva()) {
            throw new IllegalStateException(
                "No se puede responder una pregunta inactiva con ID: " + idPregunta
            );
        }
        
        // Delegar la validación al método de la entidad
        // Usado por: método validarRespuesta() en PreguntaVerdaderoFalso.java
        return pregunta.validarRespuesta(respuestaUsuario);
    }

     
    public PreguntaVerdaderoFalso obtenerPorId(Long id) {
        // Validación preventiva del ID
        if (id == null || id <= 0) {
            throw new DatosInvalidosException("id", "El ID debe ser un número positivo");
        }
        
        // Buscar y lanzar excepción si no existe
        // Manejado por GlobalExceptionHandler como HTTP 404
        return repository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "Pregunta Verdadero/Falso",
                id
            ));
    }

   
    private void validarDatosPregunta(PreguntaVerdaderoFalso pregunta) {
        // Validar que el objeto no sea nulo
        if (pregunta == null) {
            throw new DatosInvalidosException("pregunta", "La pregunta no puede ser nula");
        }
        
        // Validar enunciado obligatorio
        if (pregunta.getEnunciado() == null || pregunta.getEnunciado().trim().isEmpty()) {
            throw new DatosInvalidosException("enunciado", "El enunciado de la pregunta es obligatorio");
        }
        
        // Validar longitud mínima del enunciado
        if (pregunta.getEnunciado().trim().length() < 10) {
            throw new DatosInvalidosException(
                "enunciado", 
                "El enunciado debe tener al menos 10 caracteres"
            );
        }
        
        // Validar longitud máxima del enunciado
        if (pregunta.getEnunciado().length() > 500) {
            throw new DatosInvalidosException(
                "enunciado",
                "El enunciado no puede exceder los 500 caracteres"
            );
        }
        
        // Validar respuesta correcta obligatoria
        if (pregunta.getRespuestaCorrecta() == null) {
            throw new DatosInvalidosException(
                "respuestaCorrecta",
                "La respuesta correcta (verdadero/falso) es obligatoria"
            );
        }
        
        // Validar temática obligatoria
        if (pregunta.getTematica() == null || pregunta.getTematica().trim().isEmpty()) {
            throw new DatosInvalidosException("tematica", "La temática es obligatoria");
        }
        
        // Validar longitud máxima de la temática
        if (pregunta.getTematica().length() > 100) {
            throw new DatosInvalidosException(
                "tematica",
                "La temática no puede exceder los 100 caracteres"
            );
        }
        
        // Validar explicación (opcional, pero si existe debe tener contenido válido)
        if (pregunta.getExplicacion() != null && !pregunta.getExplicacion().trim().isEmpty()) {
            if (pregunta.getExplicacion().length() > 1000) {
                throw new DatosInvalidosException(
                    "explicacion",
                    "La explicación no puede exceder los 1000 caracteres"
                );
            }
        }
    }
}