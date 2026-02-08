package com.midominio.group.app.spring.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.midominio.group.app.spring.dto.GenerarTestRequest;
import com.midominio.group.app.spring.dto.Test;
import com.midominio.group.app.spring.entity.Pregunta;
import com.midominio.group.app.spring.service.JuegoPreguntaService;

/* Controlador REST para operaciones sobre preguntas activas y tests */

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/juego")
public class JuegoPreguntaController {

    private final JuegoPreguntaService juegoPreguntaService;

    public JuegoPreguntaController(JuegoPreguntaService preguntaService) {
        this.juegoPreguntaService = preguntaService;
    }

    /**
     * Obtiene una pregunta activa aleatoria.
     */
    @GetMapping("/preguntas/random")
    public Pregunta obtenerPreguntaAleatoria() {
        return juegoPreguntaService.obtenerPreguntaAleatoria();
    }

    /**
     * Busca preguntas activas por temática y/o tipo.     * 
     * Ejemplo:
     * http://localhost:8080/api/juego/preguntas/buscar?tematica=Astronomia&tipoPregunta=UNICA&page=0&size=10
     */
    @GetMapping("/preguntas/buscar")
    public Page<Pregunta> buscarPreguntasActivas(
            @RequestParam(required = false) String tematica,
            @RequestParam(required = false) String tipoPregunta,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        return juegoPreguntaService.buscarPreguntasActivas(tematica, tipoPregunta, pageable);
    }

    /**
     * Busca preguntas activas por texto en el enunciado.
     * Búsqueda case-insensitive (contiene el texto).
     * Ejemplo:
     * GET /api/juego/preguntas/buscar-texto/capital?page=0&size=10
     */
    @GetMapping("/preguntas/buscar-texto/{texto}")
    public Page<Pregunta> buscarPreguntasActivasPorTexto(
            @PathVariable String texto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        return juegoPreguntaService.buscarPreguntasActivasPorTexto(texto, pageable);
    }

    /**
     * Búsqueda avanzada de preguntas activas con múltiples filtros en la ruta.
     * 
     * @param texto Texto a buscar en el enunciado
     * @param tematica La temática
     * @param tipoPregunta El tipo: VF, UNICA, MULTIPLE
     * @param page Número de página (default 0)
     * @param size Tamaño de página (default 10)
     * @return Página de preguntas activas que coinciden con los criterios
     * 
     * Ejemplo:
     * GET http://localhost:8080/api/juego/preguntas/buscar-avanzado?texto=Francia&tematica=historia&tipoPregunta=UNICA&page=0&size=10
     */
    @GetMapping("/preguntas/buscar-avanzado")
    public Page<Pregunta> buscarPreguntasActivasAvanzado(
            @RequestParam(required = false) String texto,
            @RequestParam(required = false) String tematica,
            @RequestParam(required = false) String tipoPregunta,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        return juegoPreguntaService.buscarPreguntasActivasAvanzado(texto, tematica, tipoPregunta, pageable);
    }

    /**
     * Genera un test con X preguntas activas, seleccionadas aleatoriamente.
     * Soporta filtrado por temáticas y tipos de pregunta.
     * Las preguntas son sin repetición.
     * 
     * Ejemplo:
     * POST /api/juego/tests/generar
     * Body:
     * {
     *     "cantidad": 10,
     *     "tematicas": ["Historia", "Geografía"],
     *     "tipos": ["UNICA", "MULTIPLE"]
     * }
     * 
     * @param request DTO con la configuración del test
     * @return Test generado con las preguntas aleatorias
     * @throws BadRequestException si cantidad <= 0
     * @throws InsufficientQuestionsException si no hay suficientes preguntas con los filtros
     */
    @PostMapping("/tests/generar")
    public Test generarTest(@Valid @RequestBody GenerarTestRequest request) {
        return juegoPreguntaService.generarTest(
            request.getCantidad(),
            request.getTematicas(),
            request.getTipos()
        );
    }

}