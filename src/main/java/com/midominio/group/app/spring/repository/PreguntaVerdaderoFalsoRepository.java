package com.midominio.group.app.spring.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.midominio.group.app.spring.entity.PreguntaVerdaderoFalso;

import java.util.List;

/**
 * Repository específico para preguntas de tipo Verdadero/Falso.
 * Solo contiene métodos que son exclusivos de este tipo.
 * 
 * Para consultas generales (listar todas, filtrar por temática, buscar por texto, etc.)
 * usar PreguntaRepository (genérico).
 * 
 * Usado por:
 * - PreguntaVerdaderoFalsoService (para operaciones específicas de V/F)
 */
@Repository
public interface PreguntaVerdaderoFalsoRepository extends JpaRepository<PreguntaVerdaderoFalso, Long> {
    
    /**
     * Obtiene preguntas aleatorias activas de tipo Verdadero/Falso (para tests)
     * Usado en: Generación de tests que solo contengan preguntas V/F
     * 
     * Nota: Se usa Pageable para limitar la cantidad de resultados.
     * Ejemplo: PageRequest.of(0, 10) devuelve 10 preguntas aleatorias
     */
    @Query("SELECT p FROM PreguntaVerdaderoFalso p WHERE p.activa = true ORDER BY FUNCTION('RANDOM')")
    List<PreguntaVerdaderoFalso> findRandomPreguntas(Pageable pageable);
}