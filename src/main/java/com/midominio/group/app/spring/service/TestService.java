package com.midominio.group.app.spring.service;

import com.midominio.group.app.spring.dto.*;
import com.midominio.group.app.spring.entity.*;
import com.midominio.group.app.spring.exception.InsufficientQuestionsException;
import com.midominio.group.app.spring.exception.TematicaInvalidaException;
import com.midominio.group.app.spring.repository.ResultadoTestRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio que maneja la generación de tests, corrección en memoria y guardado de resultados.
 * No persiste las respuestas individuales, solo el resumen del resultado.
 */
@Service
public class TestService {
    
    private final PreguntaSearchService preguntaSearchService;
    private final ResultadoTestRepository resultadoTestRepository;
    
    // Map en memoria para almacenar tests generados temporalmente
    // clave: testId, valor: lista de preguntas del test
    private final Map<String, List<Pregunta>> testsTemporales = new HashMap<>();
    
    public TestService(PreguntaSearchService preguntaSearchService, 
                      ResultadoTestRepository resultadoTestRepository) {
        this.preguntaSearchService = preguntaSearchService;
        this.resultadoTestRepository = resultadoTestRepository;
    }
    
    /**
     * Genera un test en memoria con preguntas activas filtradas.
     * 
     * @param tematica La temática del test (requerida)
     * @param tipoPregunta Tipo de pregunta: VERDADERO_FALSO, UNICA, MULTIPLE (opcional)
     * @param cantidad Cantidad de preguntas (por defecto 10)
     * @return DTO con las preguntas sin respuestas correctas
     * @throws TematicaInvalidaException si no hay preguntas para los filtros especificados
     * @throws InsufficientQuestionsException si hay menos preguntas que la cantidad solicitada
     */
    public TestPlayDTO generarTest(String tematica, String tipoPregunta, Integer cantidad) {
        if (cantidad == null || cantidad <= 0) {
            cantidad = 10;
        }
        
        // Obtener preguntas activas con filtros dinámicos usando PreguntaSearchService
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE); // Obtener todas disponibles
        List<Pregunta> preguntas = preguntaSearchService.buscarPreguntasActivasAvanzado(
            null, tematica, tipoPregunta, pageable
        ).getContent();
        
        if (preguntas.isEmpty()) {
            String filtros = tipoPregunta != null ? 
                String.format("temática '%s' y tipo '%s'", tematica, tipoPregunta) :
                String.format("temática '%s'", tematica);
            throw new TematicaInvalidaException("No existen preguntas activas para " + filtros);
        }
        
        if (preguntas.size() < cantidad) {
            throw new InsufficientQuestionsException(
                String.format("Solo hay %d preguntas disponibles. Se solicitaron %d", 
                    preguntas.size(), cantidad)
            );
        }
        
        // Mezclar preguntas
        Collections.shuffle(preguntas);
        
        // Seleccionar la cantidad solicitada
        preguntas = preguntas.subList(0, cantidad);
        
        // Generar ID único del test
        String testId = UUID.randomUUID().toString();
        
        // Guardar el test en memoria
        testsTemporales.put(testId, preguntas);
        
        // Convertir a DTOs sin respuestas correctas
        List<PreguntaPlayDTO> preguntasDTO = preguntas.stream()
            .map(this::convertirAPreguntaPlayDTO)
            .collect(Collectors.toList());
        
        String tematicaDisplay = tipoPregunta != null ? 
            String.format("%s (%s)", tematica, tipoPregunta) : 
            tematica;
        
        return new TestPlayDTO(testId, tematicaDisplay, preguntasDTO);
    }
    

    
    /**
     * Convierte una entidad Pregunta a PreguntaPlayDTO sin revelar la respuesta correcta.
     */
    private PreguntaPlayDTO convertirAPreguntaPlayDTO(Pregunta pregunta) {
        List<String> opciones = null;
        String tipoPregunta = pregunta.getTipoPregunta();
        
        if (pregunta instanceof PreguntaUnica) {
            opciones = ((PreguntaUnica) pregunta).getOpciones();
        } else if (pregunta instanceof PreguntaMultiple) {
            opciones = ((PreguntaMultiple) pregunta).getOpciones();
        }
        // PreguntaVF no tiene opciones
        
        return new PreguntaPlayDTO(
            pregunta.getId(),
            pregunta.getEnunciado(),
            tipoPregunta,
            opciones
        );
    }
    
    /**
     * Corrige un test en memoria y devuelve el resultado con revisión detallada.
     * También guarda el resumen del resultado en la BD.
     * 
     * @param submit DTO con las respuestas del usuario
     * @param usuario Usuario que realizó el test
     * @return DTO con puntuación, corrección y explicaciones
     */
    public TestResultDTO corregirTest(TestSubmitDTO submit, Usuario usuario) {
        // Recuperar el test del almacenamiento temporal
        List<Pregunta> preguntas = testsTemporales.get(submit.getTestId());
        if (preguntas == null) {
            throw new IllegalArgumentException("Test no encontrado o expirado: " + submit.getTestId());
        }
        
        int correctas = 0;
        List<PreguntaResultDTO> revision = new ArrayList<>();
        
        // Corregir cada pregunta en memoria
        for (Pregunta pregunta : preguntas) {
            Object respuestaUsuario = submit.getRespuestas().get(pregunta.getId());
            boolean esCorrecta = validarRespuesta(pregunta, respuestaUsuario);
            
            if (esCorrecta) {
                correctas++;
            }
            
            // Construir la revisión
            PreguntaResultDTO resultadoPregunta = construirRevisionPregunta(
                pregunta, 
                respuestaUsuario, 
                esCorrecta
            );
            
            revision.add(resultadoPregunta);
        }
        
        // Calcular puntuación
        Integer puntuacion = (correctas * 100) / preguntas.size();
        
        // Guardar resultado en BD
        ResultadoTest resultado = guardarResultado(usuario, submit.getTematica(), 
                                                   puntuacion, correctas, preguntas.size());
        
        // Limpiar test temporal
        testsTemporales.remove(submit.getTestId());
        
        return new TestResultDTO(puntuacion, correctas, preguntas.size(), 
                                submit.getTematica(), revision);
    }
    
    /**
     * Valida si la respuesta del usuario es correcta según el tipo de pregunta.
     */
    private boolean validarRespuesta(Pregunta pregunta, Object respuestaUsuario) {
        if (respuestaUsuario == null) {
            return false;
        }
        
        if (pregunta instanceof PreguntaVF) {
            Boolean respuesta = null;
            if (respuestaUsuario instanceof Boolean) {
                respuesta = (Boolean) respuestaUsuario;
            } else if (respuestaUsuario instanceof String) {
                respuesta = Boolean.parseBoolean((String) respuestaUsuario);
            }
            return ((PreguntaVF) pregunta).validarRespuesta(respuesta);
            
        } else if (pregunta instanceof PreguntaUnica) {
            Integer respuesta = null;
            if (respuestaUsuario instanceof Integer) {
                respuesta = (Integer) respuestaUsuario;
            } else if (respuestaUsuario instanceof Number) {
                respuesta = ((Number) respuestaUsuario).intValue();
            }
            return ((PreguntaUnica) pregunta).validarRespuesta(respuesta);
            
        } else if (pregunta instanceof PreguntaMultiple) {
            List<Integer> respuestas = null;
            if (respuestaUsuario instanceof List) {
                respuestas = (List<Integer>) respuestaUsuario;
            }
            return ((PreguntaMultiple) pregunta).validarRespuesta(respuestas);
        }
        
        return false;
    }
    
    /**
     * Construye el DTO de revisión de una pregunta con respuestas y explicación.
     */
    private PreguntaResultDTO construirRevisionPregunta(Pregunta pregunta, 
                                                       Object respuestaUsuario, 
                                                       boolean esCorrecta) {
        Object respuestaCorrecta = obtenerRespuestaCorrecta(pregunta);
        
        return new PreguntaResultDTO(
            pregunta.getId(),
            pregunta.getEnunciado(),
            pregunta.getTipoPregunta(),
            respuestaUsuario,
            respuestaCorrecta,
            esCorrecta,
            pregunta.getExplicacion()
        );
    }
    
    /**
     * Obtiene la respuesta correcta en formato legible según el tipo de pregunta.
     */
    private Object obtenerRespuestaCorrecta(Pregunta pregunta) {
        if (pregunta instanceof PreguntaVF) {
            return ((PreguntaVF) pregunta).getRespuestaCorrecta();
        } else if (pregunta instanceof PreguntaUnica) {
            return ((PreguntaUnica) pregunta).getRespuestaCorrecta();
        } else if (pregunta instanceof PreguntaMultiple) {
            return ((PreguntaMultiple) pregunta).getRespuestasCorrectas();
        }
        return null;
    }
    
    /**
     * Guarda el resumen del test en la BD.
     * No persiste las respuestas individuales.
     */
    private ResultadoTest guardarResultado(Usuario usuario, String tematica, 
                                          Integer puntuacion, Integer correctas, 
                                          Integer cantidad) {
        ResultadoTest resultado = new ResultadoTest(usuario, tematica, puntuacion, cantidad, correctas);
        return resultadoTestRepository.save(resultado);
    }
    
    /**
     * Obtiene el historial de tests de un usuario.
     */
    public List<ResultadoTest> obtenerHistorialUsuario(Usuario usuario) {
        return resultadoTestRepository.findByUsuario(usuario);
    }
    
    /**
     * Obtiene el historial de tests de un usuario por temática.
     */
    public List<ResultadoTest> obtenerHistorialPorTematica(Usuario usuario, String tematica) {
        return resultadoTestRepository.findByUsuarioAndTematica(usuario, tematica);
    }
}
