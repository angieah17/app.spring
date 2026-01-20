package com.midominio.group.app.spring.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.midominio.group.app.spring.entity.Pregunta;

import java.util.List;
/**
* Repository genérico para todas las preguntas (clase base Pregunta).
* Contiene métodos comunes a todos los tipos de preguntas.
* 
* Usado por:
* - PreguntaService (service genérico)
* - Vistas de administración que muestran todos los tipos de preguntas
*/

@Repository
public interface PreguntaRepository extends JpaRepository<Pregunta, Long> {
   
   /**
    * Encuentra todas las preguntas activas (cualquier tipo)
    * Usado en: Vista pública general
    */
   Page<Pregunta> findByActivaTrue(Pageable pageable);
      
   /**
    * Encuentra preguntas por temática (todos los tipos)
    * Usado en: Administración con filtro de temática
    */
   Page<Pregunta> findByTematica(String tematica, Pageable pageable);
   
   /**
    * Encuentra preguntas activas por temática (todos los tipos)
    * Usado en: Vista pública con filtro de temática
    */
   Page<Pregunta> findByTematicaAndActivaTrue(String tematica, Pageable pageable);
   
   /**
    * Busca preguntas por texto en el enunciado (case insensitive)
    * Usado en: Barra de búsqueda general
    */
   @Query("SELECT p FROM Pregunta p WHERE LOWER(p.enunciado) LIKE LOWER(CONCAT('%', :texto, '%'))")
   Page<Pregunta> buscarPorEnunciado(@Param("texto") String texto, Pageable pageable);
   
   /**
    * Filtra por tipo de pregunta
    * Usado en: Administración con filtro por tipo
    */
   @Query("SELECT p FROM Pregunta p WHERE p.class = :tipo")
   Page<Pregunta> findByTipoPregunta(@Param("tipo") Class<? extends Pregunta> tipo, Pageable pageable);
   
   /**
    * Filtra por tipo y temática
    * Usado en: Filtros combinados en administración
    */
   @Query("SELECT p FROM Pregunta p WHERE p.class = :tipo AND p.tematica = :tematica")
   Page<Pregunta> findByTipoPreguntaAndTematica(
       @Param("tipo") Class<? extends Pregunta> tipo, 
       @Param("tematica") String tematica, 
       Pageable pageable
   );
   
   /**
    * Obtiene todas las temáticas distintas (de todos los tipos)
    * Usado en: Dropdowns de filtros
    */
   @Query("SELECT DISTINCT p.tematica FROM Pregunta p WHERE p.tematica IS NOT NULL ORDER BY p.tematica")
   List<String> findDistinctTematicas();
   
   /**
    * Cuenta preguntas activas (todos los tipos)
    * Usado en: Estadísticas del dashboard
    */
   long countByActivaTrue();
   
   /**
    * Cuenta preguntas por temática (todos los tipos)
    * Usado en: Estadísticas por categoría
    */
   long countByTematica(String tematica);
   
   /**
    * Cuenta preguntas por tipo
    * Usado en: Estadísticas por tipo de pregunta
    */
   @Query("SELECT COUNT(p) FROM Pregunta p WHERE p.class = :tipo")
   long countByTipoPregunta(@Param("tipo") Class<? extends Pregunta> tipo);
}
