package com.midominio.group.app.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.midominio.group.app.spring.entity.PreguntaVerdaderoFalso;
import com.midominio.group.app.spring.repository.PreguntaVerdaderoFalsoRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PreguntaVerdaderoFalsoService {

    @Autowired
    private PreguntaVerdaderoFalsoRepository repository;
    
    //Operaciones CRUD BÁSICAS
    
    /**
     * Obtiene todas las preguntas con paginación
     * Usado en: Vista de administración (listar todas las preguntas)
     */
    public Page<PreguntaVerdaderoFalso> listarTodas(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        return repository.findAll(pageable);
    }

    /**
     * Obtiene solo las preguntas activas con paginación
     * Usado en: Vista pública (mostrar preguntas disponibles para tests)
     */
    public Page<PreguntaVerdaderoFalso> listarActivas(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("fechaCreacion").descending());
        return repository.findByActivaTrue(pageable);
    }

    /**
     * Filtra preguntas por temática
     * Usado en: Filtros de búsqueda en vistas de administración y públicas
     */
    public Page<PreguntaVerdaderoFalso> filtrarPorTematica(String tematica, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("fechaCreacion").descending());
        return repository.findByTematicaAndActivaTrue(tematica, pageable);
    }

    /**
     * Busca preguntas por texto en el enunciado
     * Usado en: Barra de búsqueda en la interfaz de administración
     */
    public Page<PreguntaVerdaderoFalso> buscarPorTexto(String texto, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("fechaCreacion").descending());
        return repository.buscarPorEnunciado(texto, pageable);
    }

    /**
     * Obtiene una pregunta por ID
     * Usado en: Detalle de pregunta, edición, validación de respuesta
     */
    public Optional<PreguntaVerdaderoFalso> obtenerPorId(Long id) {
        return repository.findById(id);
    }

    /**
     * Crea una nueva pregunta
     * Usado en: Formulario de creación en la interfaz de administración
     */
    public PreguntaVerdaderoFalso crear(PreguntaVerdaderoFalso pregunta) {
        // Asegura que las nuevas preguntas estén activas por defecto
        if (pregunta.getActiva() == null) {
            pregunta.setActiva(true);
        }
        return repository.save(pregunta);
    }

    /**
     * Actualiza una pregunta existente
     * Usado en: Formulario de edición en la interfaz de administración
     */
    public PreguntaVerdaderoFalso actualizar(Long id, PreguntaVerdaderoFalso preguntaActualizada) {
        return repository.findById(id)
            .map(preguntaExistente -> {
                preguntaExistente.setEnunciado(preguntaActualizada.getEnunciado());
                preguntaExistente.setTematica(preguntaActualizada.getTematica());
                preguntaExistente.setRespuestaCorrecta(preguntaActualizada.getRespuestaCorrecta());
                preguntaExistente.setExplicacion(preguntaActualizada.getExplicacion());
                preguntaExistente.setActiva(preguntaActualizada.getActiva());
                return repository.save(preguntaExistente);
            })
            .orElseThrow(() -> new RuntimeException("Pregunta no encontrada con id: " + id));
    }

    /**
     * Elimina una pregunta (borrado físico)
     * Usado en: Botón eliminar en la interfaz de administración
     */
    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Pregunta no encontrada con id: " + id);
        }
        repository.deleteById(id);
    }

    /**
     * Desactiva una pregunta (borrado lógico)
     * Usado en: Si prefieres mantener el historial de preguntas
     */
    public void desactivar(Long id) {
        repository.findById(id)
            .map(pregunta -> {
                pregunta.setActiva(false);
                return repository.save(pregunta);
            })
            .orElseThrow(() -> new RuntimeException("Pregunta no encontrada con id: " + id));
    }

    /**
     * Reactiva una pregunta previamente desactivada
     * Usado en: Restaurar preguntas en la interfaz de administración
     */
    public void activar(Long id) {
        repository.findById(id)
            .map(pregunta -> {
                pregunta.setActiva(true);
                return repository.save(pregunta);
            })
            .orElseThrow(() -> new RuntimeException("Pregunta no encontrada con id: " + id));
    }

    /**
     * Valida la respuesta de un usuario a una pregunta
     * Usado en: Vista de test/evaluación cuando el usuario responde
     * @return true si la respuesta es correcta
     */
    public boolean validarRespuesta(Long idPregunta, Boolean respuestaUsuario) {
        return repository.findById(idPregunta)
            .map(pregunta -> pregunta.validarRespuesta(respuestaUsuario))
            .orElseThrow(() -> new RuntimeException("Pregunta no encontrada con id: " + idPregunta));
    }

    /**
     * Obtiene preguntas aleatorias para generar un test
     * Usado en: Generación automática de tests/evaluaciones
     */
    public List<PreguntaVerdaderoFalso> obtenerPreguntasAleatorias(int cantidad) {
        Pageable pageable = PageRequest.of(0, cantidad);
        return repository.findRandomPreguntas(pageable);
    }

    /**
     * Obtiene todas las temáticas distintas disponibles
     * Usado en: Dropdowns de filtros en las vistas
     */
    public List<String> obtenerTematicas() {
        return repository.findDistinctTematicas();
    }

    /**
     * Cuenta el total de preguntas activas
     * Usado en: Estadísticas del dashboard de administración
     */
    public long contarActivas() {
        return repository.countByActivaTrue();
    }

    /**
     * Cuenta preguntas por temática
     * Usado en: Estadísticas por categoría
     */
    public long contarPorTematica(String tematica) {
        return repository.countByTematica(tematica);
    }
}
