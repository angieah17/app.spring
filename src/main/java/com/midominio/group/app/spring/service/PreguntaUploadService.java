package com.midominio.group.app.spring.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.midominio.group.app.spring.dto.UploadPreguntaDTO;
import com.midominio.group.app.spring.entity.PreguntaMultiple;
import com.midominio.group.app.spring.entity.PreguntaUnica;
import com.midominio.group.app.spring.entity.PreguntaVF;
import com.midominio.group.app.spring.entity.TipoPreguntaEnum;
import com.midominio.group.app.spring.exception.BadRequestException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Service
public class PreguntaUploadService {

    private final ObjectMapper objectMapper;
    private final PreguntaVFService preguntaVFService;
    private final PreguntaUnicaService preguntaUnicaService;
    private final PreguntaMultipleService preguntaMultipleService;

    public PreguntaUploadService(
            ObjectMapper objectMapper,
            PreguntaVFService preguntaVFService,
            PreguntaUnicaService preguntaUnicaService,
            PreguntaMultipleService preguntaMultipleService) {
        this.objectMapper = objectMapper;
        this.preguntaVFService = preguntaVFService;
        this.preguntaUnicaService = preguntaUnicaService;
        this.preguntaMultipleService = preguntaMultipleService;
    }

    @Transactional
    public int upload(MultipartFile file) {
        validarArchivo(file);

        List<UploadPreguntaDTO> items = leerArchivo(file);
        if (items.isEmpty()) {
            throw new BadRequestException("El archivo no contiene preguntas");
        }

        int creadas = 0;
        for (int i = 0; i < items.size(); i++) {
            int fila = i + 1;
            UploadPreguntaDTO dto = items.get(i);
            try {
                crearPregunta(dto);
                creadas++;
            } catch (BadRequestException ex) {
                throw new BadRequestException("Error en fila " + fila + ": " + ex.getMessage());
            }
        }

        return creadas;
    }

    private void validarArchivo(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("Debe enviar un archivo no vacío");
        }

        String nombre = file.getOriginalFilename();
        if (nombre == null || !nombre.contains(".")) {
            throw new BadRequestException("No se puede identificar el tipo de archivo");
        }
    }

    private List<UploadPreguntaDTO> leerArchivo(MultipartFile file) {
        String formato = detectarFormato(file);

        try {
            if ("csv".equals(formato)) {
                return leerCsv(file);
            }
            return leerJson(file);
        } catch (IOException ex) {
            throw new BadRequestException("No se pudo leer el archivo: " + ex.getMessage());
        }
    }

    private String detectarFormato(MultipartFile file) {
        String nombre = Objects.requireNonNullElse(file.getOriginalFilename(), "").toLowerCase(Locale.ROOT);
        String contentType = Objects.requireNonNullElse(file.getContentType(), "").toLowerCase(Locale.ROOT);

        if (nombre.endsWith(".csv") || contentType.contains("csv")) {
            return "csv";
        }

        if (nombre.endsWith(".json") || contentType.contains("json")) {
            return "json";
        }

        throw new BadRequestException("Formato no soportado. Use archivos .csv o .json");
    }

    private List<UploadPreguntaDTO> leerCsv(MultipartFile file) throws IOException {
        List<UploadPreguntaDTO> resultado = new ArrayList<>();

        try (Reader reader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8);
             CSVParser parser = CSVFormat.DEFAULT
                     .builder()
                     .setHeader()
                     .setSkipHeaderRecord(true)
                     .setIgnoreEmptyLines(true)
                     .setTrim(true)
                     .build()
                     .parse(reader)) {

            for (CSVRecord record : parser) {
                UploadPreguntaDTO dto = new UploadPreguntaDTO();
                dto.setTipo(getCsv(record, "tipo"));
                dto.setTexto(getCsv(record, "texto"));
                dto.setTematica(getCsv(record, "tematica"));
                dto.setExplicacion(getCsv(record, "explicacion"));
                dto.setOpciones(TextNode.valueOf(getCsv(record, "opciones")));
                dto.setRespuestasCorrectas(TextNode.valueOf(getCsv(record, "respuestasCorrectas")));
                resultado.add(dto);
            }
        }

        return resultado;
    }

    private String getCsv(CSVRecord record, String columna) {
        if (!record.isMapped(columna)) {
            return null;
        }
        String valor = record.get(columna);
        return valor == null ? null : valor.trim();
    }

    private List<UploadPreguntaDTO> leerJson(MultipartFile file) throws IOException {
        JsonNode root = objectMapper.readTree(file.getInputStream());

        if (root == null || root.isNull()) {
            throw new BadRequestException("El JSON está vacío");
        }

        List<UploadPreguntaDTO> resultado = new ArrayList<>();

        if (root.isArray()) {
            for (JsonNode item : root) {
                resultado.add(objectMapper.convertValue(item, UploadPreguntaDTO.class));
            }
            return resultado;
        }

        if (root.isObject() && root.has("preguntas") && root.get("preguntas").isArray()) {
            for (JsonNode item : root.get("preguntas")) {
                resultado.add(objectMapper.convertValue(item, UploadPreguntaDTO.class));
            }
            return resultado;
        }

        if (root.isObject()) {
            resultado.add(objectMapper.convertValue(root, UploadPreguntaDTO.class));
            return resultado;
        }

        throw new BadRequestException("Estructura JSON inválida. Debe ser objeto o array");
    }

    private void crearPregunta(UploadPreguntaDTO dto) {
        validarComunes(dto);

        TipoPreguntaEnum tipo;
        try {
            tipo = TipoPreguntaEnum.fromValue(dto.getTipo());
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException(ex.getMessage());
        }

        if (tipo == null) {
            throw new BadRequestException("El tipo es obligatorio");
        }

        switch (tipo) {
            case VERDADERO_FALSO -> crearVF(dto);
            case UNICA -> crearUnica(dto);
            case MULTIPLE -> crearMultiple(dto);
        }
    }

    private void validarComunes(UploadPreguntaDTO dto) {
        if (dto == null) {
            throw new BadRequestException("Fila vacía");
        }
        if (isBlank(dto.getTipo())) {
            throw new BadRequestException("El campo tipo es obligatorio");
        }
        if (isBlank(dto.getTexto())) {
            throw new BadRequestException("El campo texto es obligatorio");
        }
        if (isBlank(dto.getTematica())) {
            throw new BadRequestException("El campo tematica es obligatorio");
        }
    }

    private void crearVF(UploadPreguntaDTO dto) {
        Boolean respuesta = parseBooleanRespuesta(dto.getRespuestasCorrectas());
        if (respuesta == null) {
            throw new BadRequestException("Para VERDADERO_FALSO el campo respuestasCorrectas debe indicar true/false");
        }

        PreguntaVF pregunta = new PreguntaVF();
        pregunta.setEnunciado(dto.getTexto().trim());
        pregunta.setTematica(dto.getTematica().trim());
        pregunta.setExplicacion(trimToNull(dto.getExplicacion()));
        pregunta.setRespuestaCorrecta(respuesta);
        preguntaVFService.crear(pregunta);
    }

    private void crearUnica(UploadPreguntaDTO dto) {
        List<String> opciones = parseOpciones(dto.getOpciones());
        validarOpciones(opciones);

        List<Integer> respuestas = parseIndices(dto.getRespuestasCorrectas());
        if (respuestas.size() != 1) {
            throw new BadRequestException("Para UNICA debe enviar exactamente una respuestaCorrecta");
        }

        Integer indice = respuestas.get(0);
        validarIndices(opciones, respuestas);

        PreguntaUnica pregunta = new PreguntaUnica();
        pregunta.setEnunciado(dto.getTexto().trim());
        pregunta.setTematica(dto.getTematica().trim());
        pregunta.setExplicacion(trimToNull(dto.getExplicacion()));
        pregunta.setOpciones(opciones);
        pregunta.setRespuestaCorrecta(indice);
        preguntaUnicaService.crear(pregunta);
    }

    private void crearMultiple(UploadPreguntaDTO dto) {
        List<String> opciones = parseOpciones(dto.getOpciones());
        validarOpciones(opciones);

        List<Integer> respuestas = parseIndices(dto.getRespuestasCorrectas());
        if (respuestas.isEmpty()) {
            throw new BadRequestException("Para MULTIPLE debe enviar al menos una respuestaCorrecta");
        }

        respuestas = new ArrayList<>(new LinkedHashSet<>(respuestas));
        validarIndices(opciones, respuestas);

        PreguntaMultiple pregunta = new PreguntaMultiple();
        pregunta.setEnunciado(dto.getTexto().trim());
        pregunta.setTematica(dto.getTematica().trim());
        pregunta.setExplicacion(trimToNull(dto.getExplicacion()));
        pregunta.setOpciones(opciones);
        pregunta.setRespuestasCorrectas(respuestas);
        preguntaMultipleService.crear(pregunta);
    }

    private void validarOpciones(List<String> opciones) {
        if (opciones.size() < 3) {
            throw new BadRequestException("Debe haber al menos 3 opciones");
        }
    }

    private void validarIndices(List<String> opciones, List<Integer> respuestas) {
        for (Integer indice : respuestas) {
            if (indice == null || indice < 0 || indice >= opciones.size()) {
                throw new BadRequestException("Hay respuestasCorrectas fuera de rango. Rango válido: 0 a " + (opciones.size() - 1));
            }
        }
    }

    private List<String> parseOpciones(JsonNode node) {
        if (node == null || node.isNull()) {
            throw new BadRequestException("El campo opciones es obligatorio");
        }

        List<String> opciones = new ArrayList<>();

        if (node.isArray()) {
            for (JsonNode item : node) {
                String valor = item.asText(null);
                if (!isBlank(valor)) {
                    opciones.add(valor.trim());
                }
            }
            return opciones;
        }

        if (node.isTextual()) {
            String text = node.asText();
            if (isBlank(text)) {
                return opciones;
            }

            String trimmed = text.trim();
            if (trimmed.startsWith("[")) {
                try {
                    JsonNode parsed = objectMapper.readTree(trimmed);
                    if (parsed instanceof ArrayNode arrayNode) {
                        for (JsonNode item : arrayNode) {
                            String valor = item.asText(null);
                            if (!isBlank(valor)) {
                                opciones.add(valor.trim());
                            }
                        }
                        return opciones;
                    }
                } catch (JsonProcessingException ignored) {
                }
            }

            String[] partes = trimmed.split("\\|");
            for (String parte : partes) {
                if (!isBlank(parte)) {
                    opciones.add(parte.trim());
                }
            }
            return opciones;
        }

        throw new BadRequestException("Formato inválido en opciones");
    }

    private List<Integer> parseIndices(JsonNode node) {
        if (node == null || node.isNull()) {
            throw new BadRequestException("El campo respuestasCorrectas es obligatorio");
        }

        List<Integer> indices = new ArrayList<>();

        if (node.isArray()) {
            for (JsonNode item : node) {
                if (item.isNumber()) {
                    indices.add(item.asInt());
                } else if (item.isTextual() && !isBlank(item.asText())) {
                    indices.add(parseInteger(item.asText()));
                }
            }
            return indices;
        }

        if (node.isNumber()) {
            indices.add(node.asInt());
            return indices;
        }

        if (node.isTextual()) {
            String text = node.asText();
            if (isBlank(text)) {
                return indices;
            }

            String trimmed = text.trim();
            if (trimmed.startsWith("[")) {
                try {
                    JsonNode parsed = objectMapper.readTree(trimmed);
                    if (parsed.isArray()) {
                        return parseIndices(parsed);
                    }
                } catch (JsonProcessingException ignored) {
                }
            }

            String[] partes = trimmed.split(";");
            for (String parte : partes) {
                if (!isBlank(parte)) {
                    indices.add(parseInteger(parte));
                }
            }
            return indices;
        }

        throw new BadRequestException("Formato inválido en respuestasCorrectas");
    }

    private Boolean parseBooleanRespuesta(JsonNode node) {
        if (node == null || node.isNull()) {
            return null;
        }

        if (node.isBoolean()) {
            return node.asBoolean();
        }

        if (node.isNumber()) {
            int value = node.asInt();
            if (value == 1) {
                return true;
            }
            if (value == 0) {
                return false;
            }
            return null;
        }

        if (node.isArray() && node.size() == 1) {
            return parseBooleanRespuesta(node.get(0));
        }

        if (node.isTextual()) {
            String value = node.asText("").trim().toLowerCase(Locale.ROOT);
            if (value.isEmpty()) {
                return null;
            }

            return switch (value) {
                case "true", "verdadero", "v", "si", "sí", "1" -> true;
                case "false", "falso", "f", "no", "0" -> false;
                default -> null;
            };
        }

        return null;
    }

    private Integer parseInteger(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException ex) {
            throw new BadRequestException("Valor no numérico en respuestasCorrectas: " + value);
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String trimToNull(String value) {
        return isBlank(value) ? null : value.trim();
    }
}
