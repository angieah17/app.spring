package com.midominio.group.app.spring.repository;

import com.midominio.group.app.spring.entity.Pregunta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio base para todas las preguntas.
 * Contiene únicamente consultas genéricas aplicables a todos los tipos.
 */
public interface PreguntaRepository extends JpaRepository<Pregunta, Long> {

    // Buscar todas las preguntas activas
    Page<Pregunta> findByActivaTrue(Pageable pageable);

    // Buscar preguntas por temática
    Page<Pregunta> findByTematica(String tematica, Pageable pageable);

    // Buscar preguntas activas por temática
    Page<Pregunta> findByTematicaAndActivaTrue(String tematica, Pageable pageable);

    // Buscar preguntas por tipo de pregunta (usa el getter abstracto `getTipoPregunta()`)
    Page<Pregunta> findByTipoPregunta(String tipoPregunta, Pageable pageable);

    // Contar cuántas preguntas activas existen
    long countByActivaTrue();

    // Contar preguntas por tipo de pregunta
    long countByTipoPregunta(String tipoPregunta);

}
