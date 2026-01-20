package com.midominio.group.app.spring.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones para toda la aplicación.
 * Captura excepciones lanzadas en cualquier controller y las convierte
 * en respuestas HTTP apropiadas con formato JSON consistente.
 * 
 * Usado en:
 * - Toda la API REST (/api/**)
 * - Proporciona respuestas uniformes de error
 * 
 * Excepciones manejadas:
 * - RecursoNoEncontradoException -> HTTP 404
 * - DatosInvalidosException / IllegalArgumentException -> HTTP 400
 * - IllegalStateException -> HTTP 409 (conflicto)
 * - Exception (genérica) -> HTTP 500
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * Maneja recursos no encontrados
     * Retorna HTTP 404 Not Found
     */
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> manejarRecursoNoEncontrado(
            RecursoNoEncontradoException ex, 
            WebRequest request) {
        
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Recurso no encontrado");
        body.put("mensaje", ex.getMessage());
        body.put("recurso", ex.getNombreRecurso());
        body.put("identificador", ex.getValorClave());
        body.put("path", request.getDescription(false).replace("uri=", ""));
        
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
    
    /**
     * Maneja datos de entrada inválidos
     * Retorna HTTP 400 Bad Request
     */
    @ExceptionHandler({DatosInvalidosException.class, IllegalArgumentException.class})
    public ResponseEntity<Map<String, Object>> manejarDatosInvalidos(
            RuntimeException ex, 
            WebRequest request) {
        
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Datos inválidos");
        body.put("mensaje", ex.getMessage());
        
        // Si es DatosInvalidosException, incluir el campo específico
        if (ex instanceof DatosInvalidosException) {
            String campo = ((DatosInvalidosException) ex).getCampo();
            if (campo != null) {
                body.put("campo", campo);
            }
        }
        
        body.put("path", request.getDescription(false).replace("uri=", ""));
        
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
    
    /**
     * Maneja estados ilegales (ej: intentar usar una pregunta inactiva)
     * Retorna HTTP 409 Conflict
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> manejarEstadoIlegal(
            IllegalStateException ex, 
            WebRequest request) {
        
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.CONFLICT.value());
        body.put("error", "Estado inválido");
        body.put("mensaje", ex.getMessage());
        body.put("path", request.getDescription(false).replace("uri=", ""));
        
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }
    
    /**
     * Maneja cualquier otra excepción no contemplada
     * Retorna HTTP 500 Internal Server Error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> manejarExcepcionGeneral(
            Exception ex, 
            WebRequest request) {
        
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Error interno del servidor");
        body.put("mensaje", "Ha ocurrido un error inesperado. Por favor, contacte al administrador.");
        body.put("path", request.getDescription(false).replace("uri=", ""));
        
        // En desarrollo, podrías incluir el stack trace
        // body.put("detalles", ex.getMessage());
        
        // Log del error para el equipo de desarrollo
        ex.printStackTrace();
        
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}