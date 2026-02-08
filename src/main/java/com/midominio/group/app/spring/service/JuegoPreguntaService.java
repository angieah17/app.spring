package com.midominio.group.app.spring.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.midominio.group.app.spring.entity.Pregunta;
import com.midominio.group.app.spring.exception.ResourceNotFoundException;
import com.midominio.group.app.spring.exception.TematicaInvalidaException;
import com.midominio.group.app.spring.repository.PreguntaRepository;

import static com.midominio.group.app.spring.repository.PreguntaSpecifications.*;

/* Operaciones sobre todas las preguntas activas */

@Service
public class JuegoPreguntaService {
    
    private final PreguntaRepository preguntaRepository;

    public JuegoPreguntaService(PreguntaRepository preguntaRepository) {
        this.preguntaRepository = preguntaRepository;
    }

    /**
     * Obtiene una pregunta activa aleatoria de la base de datos.
     * 
     * @return Una pregunta aleatoria que esté activa
     * @throws ResourceNotFoundException si no hay preguntas activas disponibles
     */
    public Pregunta obtenerPreguntaAleatoria() {
        long count = preguntaRepository.countByActivaTrue();
    
        if (count == 0) {
            throw new ResourceNotFoundException("No hay preguntas activas disponibles");
        }
    
        int randomOffset = (int) (Math.random() * count);
        Page<Pregunta> page = preguntaRepository.findByActivaTrue(
            PageRequest.of(randomOffset, 1)
        );
    
        return page.getContent().get(0);
    }
    

    /**
     * Obtiene preguntas activas por temática con paginación.
     * 
     * @param tematica La temática a buscar
     * @param pageable Configuración de paginación
     * @return Una página con las preguntas activas que coincidan con la temática
     * @throws TematicaInvalidaException si no existen preguntas activas con esa temática
     */
    public Page<Pregunta> obtenerPreguntasActivasPorTematica(String tematica, Pageable pageable) {
        Page<Pregunta> resultado = preguntaRepository.findByTematicaAndActivaTrue(tematica.trim(), pageable);
        
        if (resultado.isEmpty()) {
            throw new TematicaInvalidaException("No existen preguntas activas para la temática: " + tematica);
        }
        
        return resultado;
    }

    /**
     * Busca preguntas activas por temática y/o tipo de forma dinámica.
     * Los parámetros null o vacíos no se aplican como filtro.
     * 
     * @param tematica La temática a buscar (opcional)
     * @param tipoPregunta El tipo de pregunta (opcional)
     * @param pageable Configuración de paginación
     * @return Una página con las preguntas activas que coincidan con los criterios
     */
    public Page<Pregunta> buscarPreguntasActivas(String tematica, String tipoPregunta, Pageable pageable) {
        Specification<Pregunta> spec = Specification.where(activas())
                                                    .and(conTematica(tematica))
                                                    .and(conTipoPregunta(tipoPregunta));
        
        return preguntaRepository.findAll(spec, pageable);
    }

    /**
     * Busca preguntas activas por texto en el enunciado.
     * Búsqueda case-insensitive parcial (contiene).
     * 
     * @param texto El texto a buscar en los enunciados (opcional)
     * @param pageable Configuración de paginación
     * @return Una página con las preguntas activas cuyo enunciado contiene el texto
     */
    public Page<Pregunta> buscarPreguntasActivasPorTexto(String texto, Pageable pageable) {
        Specification<Pregunta> spec = Specification.where(activas())
                                                    .and(textoEnEnunciado(texto));
        
        return preguntaRepository.findAll(spec, pageable);
    }

    /**
     * Busca preguntas activas por texto, temática y/o tipo de forma dinámica.
     * Los parámetros null o vacíos no se aplican como filtro.
     * 
     * @param texto El texto a buscar en los enunciados (opcional)
     * @param tematica La temática a buscar (opcional)
     * @param tipoPregunta El tipo de pregunta (opcional)
     * @param pageable Configuración de paginación
     * @return Una página con las preguntas activas que coincidan con los criterios
     */
    public Page<Pregunta> buscarPreguntasActivasAvanzado(String texto, String tematica, String tipoPregunta, Pageable pageable) {
        Specification<Pregunta> spec = Specification.where(activas())
                                                    .and(textoEnEnunciado(texto))
                                                    .and(conTematica(tematica))
                                                    .and(conTipoPregunta(tipoPregunta));
        
        return preguntaRepository.findAll(spec, pageable);
    }

}