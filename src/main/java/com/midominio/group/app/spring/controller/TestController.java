package com.midominio.group.app.spring.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.midominio.group.app.spring.dto.GenerarTestRequest;
import com.midominio.group.app.spring.dto.Test;
import com.midominio.group.app.spring.service.JuegoPreguntaService;

/* Controlador REST para operaciones sobre tests */

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/juego/tests")
public class TestController {

    private final JuegoPreguntaService juegoPreguntaService;

    public TestController(JuegoPreguntaService juegoPreguntaService) {
        this.juegoPreguntaService = juegoPreguntaService;
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
    @PostMapping("/generar")
    public Test generarTest(@Valid @RequestBody GenerarTestRequest request) {
        return juegoPreguntaService.generarTest(
            request.getCantidad(),
            request.getTematicas(),
            request.getTipos()
        );
    }

}
