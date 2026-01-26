package com.midominio.group.app.spring.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "preguntas")
@Inheritance(strategy = InheritanceType.JOINED) //permite crear tablas separadas
@DiscriminatorColumn(name = "tipo_pregunta", discriminatorType = DiscriminatorType.STRING)
public abstract class Pregunta {
    
	//1. ATRIBUTOS
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El enunciado no puede estar vacío")
    @NotNull(message = "El enunciado es obligatorio")
    @Size(max = 500, message = "El enunciado no puede superar los 500 caracteres")
    @Column(nullable = false, length = 500)
    private String enunciado;
    
    @Column(length = 100)
    private String tematica;
    
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
    
    @Column(name = "activa") //esto permitirá el borrado lógico, es decir, no borra como tal la pregunta de la BD, la esconde
    private Boolean activa = true;
    
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }
    
    
    //2. CONSTRUCTORES
    
    public Pregunta () {
    	
    }
    
    public Pregunta(Long id, String enunciado, String tematica, LocalDateTime fechaCreacion, Boolean activa) {
		this.id = id;
		this.enunciado = enunciado;
		this.tematica = tematica;
		this.fechaCreacion = fechaCreacion;
		this.activa = activa;
	}

    
    
    //3. METODOS
	// Método abstracto que cada tipo de pregunta implementará
    public abstract String getTipoPregunta();
    
    
    


	//4. GETTERS Y SETTERS
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEnunciado() {
		return enunciado;
	}

	public void setEnunciado(String enunciado) {
		this.enunciado = enunciado;
	}

	public String getTematica() {
		return tematica;
	}

	public void setTematica(String tematica) {
		this.tematica = tematica;
	}

	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDateTime fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Boolean getActiva() {
		return activa;
	}

	public void setActiva(Boolean activa) {
		this.activa = activa;
	}


    
    //5. HASHCODE y EQUALS
    //Para mantener la persistencia en el mapeo de datos 
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