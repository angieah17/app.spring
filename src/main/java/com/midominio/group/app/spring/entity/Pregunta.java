package com.midominio.group.app.spring.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor //por lo que siempre se necesita un constructor vacío
@AllArgsConstructor //útil para crear objeto con todos los datos
@Entity
@Table(name = "preguntas")
@Inheritance(strategy = InheritanceType.JOINED) //permite crear tablas separadas
@DiscriminatorColumn(name = "tipo_pregunta", discriminatorType = DiscriminatorType.STRING)
public abstract class Pregunta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 500)
    private String enunciado;
    
    @Column(length = 100)
    private String tematica;
    
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
    
    @Column(name = "activa") //esto permitirá el borrado lógico, es decir, no borra como tal la pregunta de la BD
    private Boolean activa = true;
    
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }
    
    // Método abstracto que cada tipo de pregunta implementará
    public abstract String getTipoPregunta();
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pregunta)) return false;
        Pregunta pregunta = (Pregunta) o;
        return id != null && id.equals(pregunta.id);
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}