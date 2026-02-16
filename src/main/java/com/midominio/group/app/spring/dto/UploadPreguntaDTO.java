package com.midominio.group.app.spring.dto;

import com.fasterxml.jackson.databind.JsonNode;

public class UploadPreguntaDTO {

    private String tipo;
    private String texto;
    private String tematica;
    private String explicacion;
    private JsonNode opciones;
    private JsonNode respuestasCorrectas;

    public UploadPreguntaDTO() {
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getTematica() {
        return tematica;
    }

    public void setTematica(String tematica) {
        this.tematica = tematica;
    }

    public String getExplicacion() {
        return explicacion;
    }

    public void setExplicacion(String explicacion) {
        this.explicacion = explicacion;
    }

    public JsonNode getOpciones() {
        return opciones;
    }

    public void setOpciones(JsonNode opciones) {
        this.opciones = opciones;
    }

    public JsonNode getRespuestasCorrectas() {
        return respuestasCorrectas;
    }

    public void setRespuestasCorrectas(JsonNode respuestasCorrectas) {
        this.respuestasCorrectas = respuestasCorrectas;
    }
}
