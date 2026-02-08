package com.midominio.group.app.spring.repository;

import com.midominio.group.app.spring.entity.Pregunta;
import org.springframework.data.jpa.domain.Specification;

/**
 * Especificaciones para construir consultas dinámicas sobre Pregunta.
 * Permite combinar criterios de forma flexible (temática, tipo, activa).
 * 
 * root: obtener las columnas o hacer los joins.
 * criteriaBuilder: herramienta para construir las condiciones
 */
public class PreguntaSpecifications {

    /**
     * Filtra por temática exacta. Si es null o vacía, no aplica filtro.
     */
    public static Specification<Pregunta> conTematica(String tematica) {
        return (root, query, criteriaBuilder) -> {
            if (tematica == null || tematica.trim().isEmpty()) {
                return null; // No aplica filtro
            }
            return criteriaBuilder.equal(root.get("tematica"), tematica.trim());// // lado izquierdo: p.tematica - lado derecho: valor
        };
    }

    /**
     * Filtra por tipo de pregunta usando la columna discriminadora.
     * Si es null o vacío, no aplica filtro.
     */
    public static Specification<Pregunta> conTipoPregunta(String tipoPregunta) {
        return (root, query, criteriaBuilder) -> {
            if (tipoPregunta == null || tipoPregunta.trim().isEmpty()) {
                return null; // No aplica filtro
            }
            // Usar el nombre de la columna discriminadora definida en @DiscriminatorColumn
            return criteriaBuilder.equal(root.type(), criteriaBuilder.literal(tipoPregunta.trim()));
        };
    }

    /**
     * Filtra solo preguntas activas (activa = true).
     */
    public static Specification<Pregunta> activas() {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.isTrue(root.get("activa"));
    }

    /**
     * Filtra solo preguntas inactivas (activa = false).
     */
    public static Specification<Pregunta> inactivas() {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.isFalse(root.get("activa"));
    }
    
    //Búsqueda busqueda por texto (like)
    public static Specification<Pregunta> textoEnEnunciado(String texto) {
        return (root, query, criteriaBuilder) -> {
            if (texto == null || texto.trim().isEmpty()) {
                return null;
            }
            // WHERE LOWER(p.enunciado) LIKE '%texto%'
            return criteriaBuilder.like(
                criteriaBuilder.lower(root.get("enunciado")),
                "%" + texto.toLowerCase() + "%"
            );
        };
    }
    
    
}