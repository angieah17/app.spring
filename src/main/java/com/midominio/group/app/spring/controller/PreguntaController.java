package com.midominio.group.app.spring.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.midominio.group.app.spring.entity.Pregunta;
import com.midominio.group.app.spring.service.PreguntaService;

/* Controlador REST para operaciones sobre preguntas */

@CrossOrigin(origins = "http://localhost:5173")
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
    
    @GetMapping("/tematica/{tematica}")
    public Page<Pregunta> obtenerPreguntasPorTematica(
            @PathVariable String tematica,
            Pageable pageable) {
        return preguntaService.obtenerPreguntasPorTematica(tematica, pageable);
    }

    @GetMapping("/tematica/{tematica}/activas")
public Page<Pregunta> obtenerPreguntasActivasPorTematica(
        @PathVariable String tematica,
        Pageable pageable) {
    return preguntaService.obtenerPreguntasActivasPorTematica(tematica, pageable);
}
    
}
