package com.midominio.group.app.spring.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.midominio.group.app.spring.entity.Pregunta;
import com.midominio.group.app.spring.service.PreguntaSearchService;

/**
 * Controller REST para la administración de preguntas.
 * 
 * Proporciona endpoints para:
 * - Ver todas las preguntas (activas e inactivas)
 * - Filtrar por temática, tipo y estado de manera combinable
 * - Soporte para paginación y ordenamiento
 * 
 * Este controller está pensado para interfaces de administración con listas desplegables.
 * NO incluye lógica de juego (eso está en JuegoPreguntaController).
 */
@RestController
@RequestMapping("/api/admin/preguntas")
@CrossOrigin(origins = "*") // Ajusta según tus necesidades de CORS
public class AdminPreguntaController {
    
    private final PreguntaSearchService preguntaSearchService;

    public AdminPreguntaController(PreguntaSearchService preguntaSearchService) {
        this.preguntaSearchService = preguntaSearchService;
    }

    /**
     * Obtiene preguntas con filtros combinables.
     * 
     * Parámetros de filtrado (todos opcionales):
     * - tematica: filtra por temática exacta (String)
     * - tipo: filtra por tipo de pregunta (VERDADERO_FALSO, UNICA, MULTIPLE)
     * - activa: filtra por estado (true=activas, false=inactivas, sin parámetro=todas)
     * 
     * Parámetros de paginación (opcionales):
     * - page: número de página (por defecto 0)
     * - size: tamaño de página (por defecto 20)
     * - sort: campo y dirección de ordenamiento (por defecto id,desc)
     * 
     * Ejemplos de uso:
     * - GET /api/admin/preguntas → todas las preguntas
     * - GET /api/admin/preguntas?activa=true → solo activas
     * - GET /api/admin/preguntas?tematica=Historia → solo de Historia
     * - GET /api/admin/preguntas?tipo=MULTIPLE&activa=false → múltiples inactivas
     * - GET /api/admin/preguntas?tematica=Matemáticas&tipo=UNICA&activa=true&page=0&size=10
     * 
     * @param tematica Temática a buscar (opcional)
     * @param tipo Tipo de pregunta (opcional)
     * @param activa Estado de la pregunta (opcional)
     * @param pageable Configuración de paginación y ordenamiento
     * @return ResponseEntity con la página de preguntas que cumplen los criterios
     */
    @GetMapping
    public ResponseEntity<Page<Pregunta>> obtenerPreguntasConFiltros(
            @RequestParam(required = false) String tematica,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) Boolean activa,
            @PageableDefault(size = 10) Pageable pageable) {
        
        Page<Pregunta> resultado = preguntaSearchService.buscarConFiltros(
            tematica, 
            tipo, 
            activa, 
            pageable
        );
        
        return ResponseEntity.ok(resultado);
    }

    /**
     * Busca preguntas por texto en el enunciado, opcionalmente combinado con otros filtros.
     * 
     * Parámetros:
     * - texto: texto a buscar en el enunciado (requerido)
     * - tematica: filtra por temática (opcional)
     * - tipo: filtra por tipo de pregunta (opcional)
     * - activa: filtra por estado (opcional)
     * 
     * Ejemplo:
     * - GET /api/admin/preguntas/buscar?texto=capital&tematica=Geografia&activa=true
     * 
     * @param texto Texto a buscar en el enunciado
     * @param tematica Temática a buscar (opcional)
     * @param tipo Tipo de pregunta (opcional)
     * @param activa Estado de la pregunta (opcional)
     * @param pageable Configuración de paginación
     * @return ResponseEntity con la página de preguntas encontradas
     */
    @GetMapping("/buscar")
    public ResponseEntity<Page<Pregunta>> buscarPorTexto(
            @RequestParam String texto,
            @RequestParam(required = false) String tematica,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) Boolean activa,
            @PageableDefault(size = 10) Pageable pageable) {
        
        Page<Pregunta> resultado = preguntaSearchService.buscarPorTextoYFiltros(
            texto,
            tematica, 
            tipo, 
            activa, 
            pageable
        );
        
        return ResponseEntity.ok(resultado);
    }
}
