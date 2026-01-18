package com.midominio.group.app.spring.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.midominio.group.app.spring.entity.PreguntaVerdaderoFalso;

import java.util.List;

@Repository
public interface PreguntaVerdaderoFalsoRepository extends JpaRepository<PreguntaVerdaderoFalso, Long> {
    
	
	//I. MÉTODOS DERIVADOS DE Spring Data JPA
    

     //1. Encuentra todas las preguntas activas
     
    Page<PreguntaVerdaderoFalso> findByActivaTrue(Pageable pageable);
    
     //2. Encuentra preguntas por temática

    Page<PreguntaVerdaderoFalso> findByTematica(String tematica, Pageable pageable);
    
     //3. Encuentra preguntas activas por temática
 
    Page<PreguntaVerdaderoFalso> findByTematicaAndActivaTrue(String tematica, Pageable pageable);
    
     //4. Cuenta preguntas activas para estadisticas
     
    long countByActivaTrue();
    
    //5. Cuenta preguntas por temática para estadisticas

    long countByTematica(String tematica);
    
    //2. MÉTODOS PERSONALIZADOS
    
     //1. Busca preguntas por texto en el enunciado (case insensitive)

    @Query("SELECT p FROM PreguntaVerdaderoFalso p WHERE LOWER(p.enunciado) LIKE LOWER(CONCAT('%', :texto, '%'))")
    Page<PreguntaVerdaderoFalso> buscarPorEnunciado(@Param("texto") String texto, Pageable pageable);
    
     //2. Lista todas las temáticas disponibles (para libros)
    
    @Query("SELECT DISTINCT p.tematica FROM PreguntaVerdaderoFalso p WHERE p.tematica IS NOT NULL ORDER BY p.tematica")
    List<String> findDistinctTematicas();
    

     //3. Obtiene N preguntas aleatorias activas (para tests) Se tiene que cambiar en MYSQL
    
    @Query("SELECT p FROM PreguntaVerdaderoFalso p WHERE p.activa = true ORDER BY FUNCTION('RANDOM')")
    List<PreguntaVerdaderoFalso> findRandomPreguntas(Pageable pageable);
}
