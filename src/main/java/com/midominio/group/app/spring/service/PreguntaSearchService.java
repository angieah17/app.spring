package com.midominio.group.app.spring.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.midominio.group.app.spring.entity.Pregunta;
import com.midominio.group.app.spring.entity.PreguntaMultiple;
import com.midominio.group.app.spring.entity.PreguntaUnica;
import com.midominio.group.app.spring.entity.PreguntaVF;
import com.midominio.group.app.spring.exception.ResourceNotFoundException;
import com.midominio.group.app.spring.repository.PreguntaRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.midominio.group.app.spring.repository.PreguntaSpecifications.*;

/**
 * Servicio de búsqueda y gestión de preguntas para administración.
 * Permite combinar múltiples filtros de forma flexible (temática, tipo, estado).
 * 
 * A diferencia de TestService, este servicio:
 * - No filtra solo activas por defecto
 * - Permite ver tanto activas como inactivas
 * - Está enfocado en operaciones de administración (lectura, activación/desactivación)
 * 
 * NOTA: Para crear y editar preguntas, usar los servicios específicos por tipo
 * (PreguntaVFService, PreguntaUnicaService, PreguntaMultipleService) ya que
 * cada tipo tiene campos específicos diferentes (respuestas, opciones, etc).
 */
@Service
public class PreguntaSearchService {
    
    private final PreguntaRepository preguntaRepository;

    public PreguntaSearchService(PreguntaRepository preguntaRepository) {
        this.preguntaRepository = preguntaRepository;
    }

    /**
     * Busca preguntas combinando múltiples filtros opcionales.
     * Todos los parámetros son opcionales (null o vacío = no aplica ese filtro).
     * 
     * Filtros disponibles:
     * - tematica: filtra por temática exacta
     * - tipoPregunta: filtra por tipo (VERDADERO_FALSO, UNICA, MULTIPLE)
     * - activa: filtra por estado (true=activas, false=inactivas, null=todas)
     * 
     * @param tematica La temática a buscar (opcional)
     * @param tipoPregunta El tipo de pregunta (opcional)
     * @param activa El estado de la pregunta (opcional: true, false o null)
     * @param pageable Configuración de paginación y ordenamiento
     * @return Una página con las preguntas que cumplan los criterios
     */
    public Page<Pregunta> buscarConFiltros(
            String tematica, 
            String tipoPregunta, 
            Boolean activa, 
            Pageable pageable) {
        
        // Construir la especificación combinando filtros atómicos
        // Los filtros null o vacíos se ignoran automáticamente
        Specification<Pregunta> spec = Specification.where(conTematica(tematica))
                                                    .and(conTipoPregunta(tipoPregunta))
                                                    .and(conEstado(activa));
        
        return preguntaRepository.findAll(spec, pageable);
    }

    /**
     * Obtiene todas las preguntas sin filtros (activas e inactivas).
     * 
     * @param pageable Configuración de paginación y ordenamiento
     * @return Una página con todas las preguntas
     */
    public Page<Pregunta> obtenerTodas(Pageable pageable) {
        return preguntaRepository.findAll(pageable);
    }

    /**
     * Busca preguntas por texto en el enunciado, opcionalmente combinado con otros filtros.
     * Búsqueda case-insensitive parcial (contiene).
     * 
     * @param texto El texto a buscar en los enunciados (opcional)
     * @param tematica La temática a buscar (opcional)
     * @param tipoPregunta El tipo de pregunta (opcional)
     * @param activa El estado de la pregunta (opcional)
     * @param pageable Configuración de paginación y ordenamiento
     * @return Una página con las preguntas que cumplan los criterios
     */
    public Page<Pregunta> buscarPorTextoYFiltros(
            String texto,
            String tematica, 
            String tipoPregunta, 
            Boolean activa, 
            Pageable pageable) {
        
        Specification<Pregunta> spec = Specification.where(textoEnEnunciado(texto))
                                                    .and(conTematica(tematica))
                                                    .and(conTipoPregunta(tipoPregunta))
                                                    .and(conEstado(activa));
        
        return preguntaRepository.findAll(spec, pageable);
    }

    /**
     * Obtiene una pregunta por su ID.
     * 
     * @param id El ID de la pregunta
     * @return La pregunta encontrada
     * @throws ResourceNotFoundException si no existe la pregunta
     */
    public Pregunta obtenerPorId(Long id) {
        return preguntaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pregunta no encontrada con id: " + id));
    }

    /**
     * Activa una pregunta (soft delete inverso).
     * 
     * @param id El ID de la pregunta a activar
     * @return La pregunta activada
     * @throws ResourceNotFoundException si no existe la pregunta
     */
    public Pregunta activar(Long id) {
        Pregunta pregunta = obtenerPorId(id);
        pregunta.setActiva(true);
        return preguntaRepository.save(pregunta);
    }

    /**
     * Desactiva una pregunta (soft delete).
     * 
     * @param id El ID de la pregunta a desactivar
     * @return La pregunta desactivada
     * @throws ResourceNotFoundException si no existe la pregunta
     */
    public Pregunta desactivar(Long id) {
        Pregunta pregunta = obtenerPorId(id);
        pregunta.setActiva(false);
        return preguntaRepository.save(pregunta);
    }

    public int importarDesdeCSV(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("El archivo está vacío");
        }

        int creadas = 0;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            boolean primeraLinea = true;

            while ((line = reader.readLine()) != null) {
                if (primeraLinea) {
                    primeraLinea = false;
                    continue;
                }

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] data = line.split(";", -1);

                String tipo = valor(data, 0);
                String enunciado = valor(data, 1);
                String tematica = valor(data, 2);

                if (tipo == null || tipo.isEmpty()) {
                    throw new RuntimeException("Tipo no válido");
                }
                if (enunciado == null || enunciado.isEmpty()) {
                    throw new RuntimeException("Enunciado vacío");
                }
                if (tematica == null || tematica.isEmpty()) {
                    throw new RuntimeException("Temática vacía");
                }

                Pregunta pregunta = crearPregunta(tipo, data);
                pregunta.setEnunciado(enunciado);
                pregunta.setTematica(tematica);
                pregunta.setExplicacion(valor(data, 3));

                preguntaRepository.save(pregunta);
                creadas++;
            }

            return creadas;
        } catch (IOException e) {
            throw new RuntimeException("Error procesando archivo", e);
        }
    }

    private Pregunta crearPregunta(String tipo, String[] data) {
        return switch (tipo) {
            case "VERDADERO_FALSO" -> {
                PreguntaVF preguntaVF = new PreguntaVF();
                preguntaVF.setRespuestaCorrecta(parseBoolean(valor(data, 5)));
                yield preguntaVF;
            }
            case "UNICA" -> {
                PreguntaUnica preguntaUnica = new PreguntaUnica();
                preguntaUnica.setOpciones(parseOpciones(valor(data, 4)));
                preguntaUnica.setRespuestaCorrecta(parseIndiceUnico(valor(data, 5)));
                yield preguntaUnica;
            }
            case "MULTIPLE" -> {
                PreguntaMultiple preguntaMultiple = new PreguntaMultiple();
                preguntaMultiple.setOpciones(parseOpciones(valor(data, 4)));
                preguntaMultiple.setRespuestasCorrectas(parseIndicesMultiples(valor(data, 5)));
                yield preguntaMultiple;
            }
            default -> throw new RuntimeException("Tipo de pregunta no soportado: " + tipo);
        };
    }

    private String valor(String[] data, int indice) {
        if (indice < 0 || indice >= data.length) {
            return null;
        }
        String valor = data[indice];
        return valor == null ? null : valor.trim();
    }

    private List<String> parseOpciones(String texto) {
        List<String> opciones = new ArrayList<>();
        if (texto == null || texto.isBlank()) {
            return opciones;
        }

        String[] partes = texto.split("\\|");
        for (String parte : partes) {
            String limpia = parte == null ? null : parte.trim();
            if (limpia != null && !limpia.isEmpty()) {
                opciones.add(limpia);
            }
        }
        return opciones;
    }

    private Integer parseIndiceUnico(String texto) {
        if (texto == null || texto.isBlank()) {
            return null;
        }

        String limpio = texto.split(";")[0].trim();
        return Integer.parseInt(limpio);
    }

    private List<Integer> parseIndicesMultiples(String texto) {
        List<Integer> respuestas = new ArrayList<>();
        if (texto == null || texto.isBlank()) {
            return respuestas;
        }

        String[] partes = texto.split(";");
        for (String parte : partes) {
            String limpia = parte == null ? null : parte.trim();
            if (limpia != null && !limpia.isEmpty()) {
                respuestas.add(Integer.parseInt(limpia));
            }
        }
        return respuestas;
    }

    private Boolean parseBoolean(String texto) {
        if (texto == null || texto.isBlank()) {
            return null;
        }

        String valor = texto.trim().toLowerCase();
        return switch (valor) {
            case "true", "verdadero", "1", "v" -> true;
            case "false", "falso", "0", "f" -> false;
            default -> throw new RuntimeException("Valor booleano inválido: " + texto);
        };
    }
}

