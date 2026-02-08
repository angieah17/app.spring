package com.midominio.group.app.spring.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.midominio.group.app.spring.entity.Pregunta;
import com.midominio.group.app.spring.service.JuegoPreguntaService;

/* Controlador REST para operaciones sobre preguntas */

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/juego/preguntas")
public class JuegoPreguntaController {

    private final JuegoPreguntaService juegoPreguntaService;

    public JuegoPreguntaController(JuegoPreguntaService preguntaService) {
        this.juegoPreguntaService = preguntaService;
    }

    @GetMapping("/random")
    public Pregunta obtenerPreguntaAleatoria() {
        return juegoPreguntaService.obtenerPreguntaAleatoria();
    }


    @GetMapping("/tematica/{tematica}/activas")
public Page<Pregunta> obtenerPreguntasActivasPorTematica(
        @PathVariable String tematica,
        Pageable pageable) {
    return juegoPreguntaService.obtenerPreguntasActivasPorTematica(tematica, pageable);
}
    
}
