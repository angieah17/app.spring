package com.midominio.group.app.spring.service;

import com.midominio.group.app.spring.entity.PreguntaVerdaderoFalso;
import com.midominio.group.app.spring.exception.DatosInvalidosException;
import com.midominio.group.app.spring.exception.RecursoNoEncontradoException;
import com.midominio.group.app.spring.repository.PreguntaVerdaderoFalsoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service específico para preguntas de tipo Verdadero/Falso.
 * Solo contiene métodos que son exclusivos de este tipo de pregunta.
 * 
 * Hereda de:
 * - AbstractPreguntaService<PreguntaVerdaderoFalso> para funcionalidades comunes
 * 
 * Para operaciones genéricas (listar, filtrar, eliminar, etc.) usar:
 * - PreguntaService (genérico, polimórfico para todas las preguntas)
 * 
 * Usado en:
 * - PreguntaVerdaderoFalsoController (API REST) para operaciones CRUD específicas
 * - TestService para validación de respuestas en evaluaciones
 * - Generación de tests aleatorios con solo preguntas V/F
 * 
 * Excepciones lanzadas:
 * - RecursoNoEncontradoException: cuando no existe una pregunta con el ID solicitado
 * - DatosInvalidosException: cuando los datos de entrada no cumplen las validaciones
 * - IllegalStateException: cuando se intenta usar una pregunta inactiva
 */
@Service
@Transactional
public class PreguntaVerdaderoFalsoService extends AbstractPreguntaService<PreguntaVerdaderoFalso> {
    
    @Autowired
    private PreguntaVerdaderoFalsoRepository repository;
    
    @Override
    protected JpaRepository<PreguntaVerdaderoFalso, Long> getRepository() {
        return repository;
    }
    
    /**
     * Crea una nueva pregunta de tipo Verdadero/Falso
     * 
     * Usado en: 
     * - API REST POST /api/preguntas/verdadero-falso
     * - Importación masiva de preguntas desde archivo
     * 
     * @param pregunta objeto con los datos de la nueva pregunta
     * @return la pregunta guardada con su ID generado
     * @throws DatosInvalidosException si los datos de entrada son inválidos
     */
    public PreguntaVerdaderoFalso crear(PreguntaVerdaderoFalso pregunta) {
        // Validación preventiva de datos de entrada
        // Lanza DatosInvalidosException si hay errores
        validarDatosPregunta(pregunta);
        
        // Asegura que las nuevas preguntas estén activas por defecto
        if (pregunta.getActiva() == null) {
            pregunta.setActiva(true);
        }
        
        return repository.save(pregunta);
    }
    
    /**
     * Actualiza una pregunta de tipo Verdadero/Falso existente
     * 
     * Usado en: 
     * - API REST PUT /api/preguntas/verdadero-falso/{id}
     * - Edición manual desde el panel de administración
     * 
     * @param id identificador de la pregunta a actualizar
     * @param preguntaActualizada datos actualizados
     * @return la pregunta actualizada
     * @throws RecursoNoEncontradoException si no existe la pregunta con ese ID
     * @throws DatosInvalidosException si los datos de entrada son inválidos
     */
    public PreguntaVerdaderoFalso actualizar(Long id, PreguntaVerdaderoFalso preguntaActualizada) {
        validarIdPositivo(id);
        validarDatosPregunta(preguntaActualizada);
        
        PreguntaVerdaderoFalso preguntaExistente = repository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "Pregunta Verdadero/Falso", 
                id
            ));
        
        preguntaExistente.setEnunciado(preguntaActualizada.getEnunciado());
        preguntaExistente.setTematica(preguntaActualizada.getTematica());
        preguntaExistente.setActiva(preguntaActualizada.getActiva());
        preguntaExistente.setRespuestaCorrecta(preguntaActualizada.getRespuestaCorrecta());
        preguntaExistente.setExplicacion(preguntaActualizada.getExplicacion());
        
        return repository.save(preguntaExistente);
    }
    
    /**
     * Valida si la respuesta de un usuario a una pregunta V/F es correcta
     * 
     * Usado en: 
     * - TestService al procesar respuestas de evaluaciones
     * - Validación en tiempo real durante tests
     * 
     * @param idPregunta ID de la pregunta
     * @param respuestaUsuario respuesta del usuario (true/false)
     * @return true si la respuesta es correcta, false si es incorrecta
     * @throws RecursoNoEncontradoException si no existe la pregunta
     * @throws DatosInvalidosException si los parámetros son inválidos
     * @throws IllegalStateException si la pregunta está inactiva
     */
    public boolean validarRespuesta(Long idPregunta, Boolean respuestaUsuario) {
        validarIdPositivo(idPregunta);
        validarRespuestaUsuario(respuestaUsuario);
        
        PreguntaVerdaderoFalso pregunta = repository.findById(idPregunta)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "Pregunta Verdadero/Falso",
                idPregunta
            ));
        
        validarPreguntaActiva(pregunta, idPregunta);
        return pregunta.validarRespuesta(respuestaUsuario);
    }
    
    /**
     * Obtiene una pregunta por ID con validación
     * 
     * Usado en: 
     * - Controllers que necesitan obtener una pregunta específica
     * - Generación de vistas de detalle de pregunta
     * 
     * @param id identificador de la pregunta
     * @return la pregunta encontrada
     * @throws RecursoNoEncontradoException si no existe
     * @throws DatosInvalidosException si el ID es inválido
     */
    public PreguntaVerdaderoFalso obtenerPorId(Long id) {
        return super.obtenerPorId(id);
    }
    
    /**
     * Valida datos específicos de una pregunta Verdadero/Falso
     * Valida: enunciado, temática, respuesta correcta y explicación
     * 
     * Llamado desde:
     * - crear()
     * - actualizar()
     * 
     * @param pregunta pregunta a validar
     * @throws DatosInvalidosException si los datos son inválidos
     */
    private void validarDatosPregunta(PreguntaVerdaderoFalso pregunta) {
        validarNoNulo(pregunta, "Pregunta");
        validarEnunciado(pregunta.getEnunciado());
        validarRespuestaCorrecta(pregunta.getRespuestaCorrecta());
        validarTematica(pregunta.getTematica());
        validarExplicacion(pregunta.getExplicacion());
    }
    
    /**
     * Valida que la respuesta correcta no sea nula
     * Específico de preguntas Verdadero/Falso
     * 
     * @param respuestaCorrecta respuesta a validar
     * @throws DatosInvalidosException si la respuesta es nula
     */
    private void validarRespuestaCorrecta(Boolean respuestaCorrecta) {
        if (respuestaCorrecta == null) {
            throw new DatosInvalidosException(
                "respuestaCorrecta",
                "La respuesta correcta (verdadero/falso) es obligatoria"
            );
        }
    }
}