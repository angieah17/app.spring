package com.midominio.group.app.spring.controller;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	
	private final PreguntaVFService service;
	
	//inyección de dependencias por constructor y no autowired para garantizar la inmutabilidad
    public PreguntaVFController(PreguntaVFService service) {
        this.service = service;
    }
    
    // GET /api/preguntas/vf?page=0&size=10
    @GetMapping
    public Page<PreguntaVF> listar(Pageable pageable) {
        return service.findAll(pageable);
    }
    
    // GET /api/preguntas/vf/{id}
    @GetMapping("/{id}")
    public ResponseEntity<PreguntaVF> obtener(@PathVariable Long id) {
    	
        Optional<PreguntaVF> pregunta = service.findById(id);
        
        return pregunta.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build()); // cuando se hagan excepciones más específicas se pueden mostrar estados HTTP más específicos
    }
    
    // POST /api/preguntas/vf
    @PostMapping
    public PreguntaVF crear(@RequestBody PreguntaVF pregunta) {
        return service.crear(pregunta); 
        //Más adelante se puede incluir validaciones como que la respuesta no es null
    }

    
}
