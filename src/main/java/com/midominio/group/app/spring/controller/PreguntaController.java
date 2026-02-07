package com.midominio.group.app.spring.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.midominio.group.app.spring.entity.Pregunta;
import com.midominio.group.app.spring.service.PreguntaService;

/* Controlador REST para operaciones sobre preguntas */

@RestController
@RequestMapping("/api/preguntas")
public class PreguntaController {

    private final PreguntaService preguntaService;

    public PreguntaController(PreguntaService preguntaService) {
        this.preguntaService = preguntaService;
    }

    @GetMapping("/random")
    public Pregunta obtenerPreguntaAleatoria() {
        return preguntaService.obtenerPreguntaAleatoria();
    }
}
