package com.midominio.group.app.spring.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.midominio.group.app.spring.entity.Pregunta;
import com.midominio.group.app.spring.entity.PreguntaVF;
import com.midominio.group.app.spring.service.PreguntaSearchService;
import com.midominio.group.app.spring.service.PreguntaVFService;

/**
 * Controlador ADMINISTRATIVO para gestión de preguntas.
 * 
 * Acceso: Solo administradores
 * Responsabilidades:
 * - Crear, editar, eliminar preguntas
 * - Filtrar y buscar preguntas
 * - Gestionar preguntas activas/inactivas
 * - Ver todas las preguntas (incluyendo respuestas correctas)
 * 
 * Base URL: /api/admin/preguntas
 */
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/admin/preguntas")
public class PreguntaAdminController {

    private final PreguntaSearchService preguntaSearchService;
    private final PreguntaVFService preguntaVFService;
    // TODO: Inyectar PreguntaUnicaService, PreguntaMultipleService para CRUD

    public PreguntaAdminController(PreguntaSearchService preguntaSearchService,
                                   PreguntaVFService preguntaVFService) {
        this.preguntaSearchService = preguntaSearchService;
        this.preguntaVFService = preguntaVFService;
    }

    /**
     * Búsqueda avanzada de preguntas (incluyendo inactivas).
     * Filtros opcionales: texto, temática, tipo de pregunta.
     * 
     * Ejemplo:
     * GET http://localhost:8080/api/admin/preguntas/buscar?texto=Java&&tematica=Programación&tipoPregunta=VERDADERO_FALSO&page=0&size=10
     */
    @GetMapping("/buscar")
    public Page<Pregunta> buscarPreguntasAvanzado(
            @RequestParam(required = false) String texto,
            @RequestParam(required = false) String tematica,
            @RequestParam(required = false) String tipoPregunta,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        return preguntaSearchService.buscarTodasLasPreguntas(texto, tematica, tipoPregunta, pageable);
    }

    /**
     * Obtiene todas las preguntas de una temática (activas e inactivas).
     * Útil para administradores que necesitan ver el historial completo.
     * 
     * http://localhost:8080/api/admin/preguntas/tematica/Programación
     */
    @GetMapping("/tematica/{tematica}")
    public Page<Pregunta> obtenerPreguntasPorTematica(
            @PathVariable String tematica,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        return preguntaSearchService.obtenerTodasPorTematica(tematica, pageable);
    }

    /**
     * CRUD: Crear una nueva pregunta.
     * 
     * Ejemplo [POST /api/admin/preguntas/vf]:
     * {
     *     "enunciado": "¿La Tierra es redonda?",
     *     "tematica": "Astronomía",
     *     "respuestaCorrecta": true,
     *     "explicacion": "La Tierra es un esferoide oblato..."
     * }
     */
    @PostMapping("/vf")
    public ResponseEntity<PreguntaVF> crearPreguntaVF(@Valid @RequestBody PreguntaVF pregunta) {
        PreguntaVF preguntaCreada = preguntaVFService.crear(pregunta);
        return ResponseEntity.status(HttpStatus.CREATED).body(preguntaCreada);
    }

    /**
     * CRUD: Crear una nueva pregunta de respuesta única.
     */
    @PostMapping("/unica")
    public ResponseEntity<String> crearPreguntaUnica() {
        // TODO: Implementar con PreguntaUnicaService
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
            .body("Endpoint en desarrollo. Usar PreguntaUnicaController.");
    }

    /**
     * CRUD: Crear una nueva pregunta de múltiples respuestas.
     */
    @PostMapping("/multiple")
    public ResponseEntity<String> crearPreguntaMultiple() {
        // TODO: Implementar con PreguntaMultipleService
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
            .body("Endpoint en desarrollo. Usar PreguntaMultipleController.");
    }

    /**
     * CRUD: Editar una pregunta existente.
     * 
     * PUT /api/admin/preguntas/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> editarPregunta(@PathVariable Long id) {
        // TODO: Implementar
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
            .body("Endpoint en desarrollo");
    }

    /**
     * CRUD: Desactivar una pregunta (soft delete).
     * 
     * DELETE /api/admin/preguntas/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> desactivarPregunta(@PathVariable Long id) {
        // TODO: Implementar con AbstractPreguntaService.desactivar()
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
            .body("Endpoint en desarrollo");
    }

    /**
     * UTILIDAD: Reactivar una pregunta desactivada.
     * 
     * PATCH /api/admin/preguntas/{id}/activar
     */
    @PatchMapping("/{id}/activar")
    public ResponseEntity<String> activarPregunta(@PathVariable Long id) {
        // TODO: Implementar
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
            .body("Endpoint en desarrollo");
    }
}
