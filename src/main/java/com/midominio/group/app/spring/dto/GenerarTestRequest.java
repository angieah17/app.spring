package com.midominio.group.app.spring.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * DTO para solicitar la generación de un test con preguntas.
 * Soporta filtros opcionales por temática y tipo de pregunta.
 */
public class GenerarTestRequest {
    
    @NotNull(message = "La cantidad de preguntas es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;
    
    private List<String> tematicas;
    
    private List<String> tipos;
    
    public GenerarTestRequest() {
    }
    
    public GenerarTestRequest(Integer cantidad, List<String> tematicas, List<String> tipos) {
        this.cantidad = cantidad;
        this.tematicas = tematicas;
        this.tipos = tipos;
    }
    
    public Integer getCantidad() {
        return cantidad;
    }
    
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
    
    public List<String> getTematicas() {
        return tematicas;
    }
    
    public void setTematicas(List<String> tematicas) {
        this.tematicas = tematicas;
    }
    
    public List<String> getTipos() {
        return tipos;
    }
    
    public void setTipos(List<String> tipos) {
        this.tipos = tipos;
    }
}
