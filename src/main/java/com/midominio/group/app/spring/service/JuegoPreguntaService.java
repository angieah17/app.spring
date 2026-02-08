package com.midominio.group.app.spring.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.midominio.group.app.spring.dto.Test;
import com.midominio.group.app.spring.entity.Pregunta;
import com.midominio.group.app.spring.exception.BadRequestException;
import com.midominio.group.app.spring.exception.InsufficientQuestionsException;
import com.midominio.group.app.spring.exception.ResourceNotFoundException;
import com.midominio.group.app.spring.exception.TematicaInvalidaException;
import com.midominio.group.app.spring.repository.PreguntaRepository;

import static com.midominio.group.app.spring.repository.PreguntaSpecifications.*;

import java.util.Collections;
import java.util.List;

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

    /**
     * Genera un test con X preguntas activas y seleccionadas aleatoriamente.
     * Soporta filtrado por una o varias temáticas y tipos de pregunta.
     * 
     * @param cantidad Número de preguntas para el test
     * @param tematicas Lista de temáticas a incluir (opcional). Si es null o vacía, incluye todas
     * @param tipos Lista de tipos de pregunta (VF, UNICA, MULTIPLE) (opcional). Si es null o vacía, incluye todos
     * @return Un Test con las preguntas seleccionadas aleatoriamente
     * @throws BadRequestException si cantidad <= 0
     * @throws InsufficientQuestionsException si no hay suficientes preguntas activas con los filtros especificados
     */
    public Test generarTest(Integer cantidad, List<String> tematicas, List<String> tipos) {
        // 1. VALIDAR CANTIDAD
        if (cantidad == null || cantidad <= 0) {
            throw new BadRequestException("La cantidad de preguntas debe ser mayor a 0");
        }
        
        // 2. CONSTRUIR SPECIFICATION DINÁMICAMENTE
        Specification<Pregunta> spec = Specification.where(activas());
        
        // Añadir filtro de temáticas si se proporcionan
        if (tematicas != null && !tematicas.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> 
                root.get("tematica").in(tematicas)
            );
        }
        
        // Añadir filtro de tipos si se proporcionan
        if (tipos != null && !tipos.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> 
                root.type().in(tipos.toArray(new String[0]))
            );
        }
        
        // 3. CONTAR PREGUNTAS DISPONIBLES
        long disponibles = preguntaRepository.count(spec);
        
        if (disponibles < cantidad) {
            String mensaje = String.format(
                "No hay suficientes preguntas activas. Solicitadas: %d, Disponibles: %d", 
                cantidad, disponibles
            );
            throw new InsufficientQuestionsException(mensaje);
        }
        
        // 4. OBTENER TODAS LAS PREGUNTAS QUE CUMPLEN LOS FILTROS
        List<Pregunta> todasDisponibles = preguntaRepository.findAll(spec);
        
        // 5. SELECCIONAR ALEATORIAMENTE SIN REPETICIÓN
        List<Pregunta> preguntasSeleccionadas = seleccionarAleatoriamente(todasDisponibles, cantidad);
        
        // 6. CREAR Y DEVOLVER EL TEST
        return new Test(preguntasSeleccionadas);
    }

    /**
     * Selecciona una cantidad de elementos aleatorios de una lista sin repetición.
     * 
     * @param lista Lista origen
     * @param cantidad Cantidad a seleccionar
     * @return Nueva lista con elementos aleatorios sin repetición
     */
    private List<Pregunta> seleccionarAleatoriamente(List<Pregunta> lista, int cantidad) {
        // Copiar la lista para no modificar la original
        List<Pregunta> copia = new java.util.ArrayList<>(lista);
        
        // Mezclar (shuffle) la lista
        Collections.shuffle(copia);
        
        // Devolver solo los primeros 'cantidad' elementos
        return copia.subList(0, cantidad);
    }

}