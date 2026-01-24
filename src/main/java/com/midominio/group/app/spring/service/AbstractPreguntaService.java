package com.midominio.group.app.spring.service;

import com.midominio.group.app.spring.entity.Pregunta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Servicio abstracto con operaciones comunes a todas las preguntas.
 * Implementa solo lógica genérica (no lógica de negocio específica).
 * 
 * Se garantiza el tipado fuerte usando el repositorio también tipado. 
 */
public abstract class AbstractPreguntaService<T extends Pregunta> {

    protected final JpaRepository<T, Long> repository;

    protected AbstractPreguntaService(JpaRepository<T, Long> repository) {
        this.repository = repository;
    }
    
    public T crear(T pregunta) {
        // reglas comunes a todas las preguntas
        pregunta.setId(null);      // seguridad: forzar INSERT
        pregunta.setActiva(true);  // por defecto activa
        return repository.save(pregunta);
    }

    public Optional<T> findById(Long id) {
        return repository.findById(id);
    }

    public Page<T> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    //Se usa soft delete, en vez de eliminar campos de la base de datos, se hace un borrado lógico.
    public T desactivar(Long id) {
        T pregunta = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pregunta no encontrada")); //Por ahora excepciones genéricas, en una fase más avanzada se especificarán

        pregunta.setActiva(false);
        return repository.save(pregunta);
    }

    public T activar(Long id) {
        T pregunta = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pregunta no encontrada"));

        pregunta.setActiva(true);
        return repository.save(pregunta);
    }

    public long count() {
        return repository.count();
    }
}
