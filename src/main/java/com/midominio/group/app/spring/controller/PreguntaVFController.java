package com.midominio.group.app.spring.controller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.midominio.group.app.spring.entity.PreguntaVF;
import com.midominio.group.app.spring.service.PreguntaVFService;


/* Controlador específico para preguntas Verdadero/Falso.
Se sirve de AbstractPreguntaService pues esta clase tiene métodos genéricos para todas las preguntas.
*/

@RestController
@RequestMapping("/api/preguntas/vf")
public class PreguntaVFController {
	
    private PreguntaVFService service;
    
    // GET /api/preguntas/vf?page=0&size=10
    @GetMapping
    public Page<PreguntaVF> listar(Pageable pageable) {
        return service.findAll(pageable);
    }
    
    
}
