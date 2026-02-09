package com.midominio.group.app.spring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.midominio.group.app.spring.dto.TestPlayDTO;
import com.midominio.group.app.spring.dto.TestResultDTO;
import com.midominio.group.app.spring.dto.TestSubmitDTO;
import com.midominio.group.app.spring.service.TestService;

import jakarta.validation.Valid;

/**
 * Controlador REST para gestión de tests evaluables.
 * 
 * Endpoints:
 * - GET /api/tests - Genera un nuevo test con filtros opcionales
 * - POST /api/tests/submit - Corrige un test y guarda el resultado
 * 
 * Requiere autenticación para todos los endpoints.
 */
@RestController
@RequestMapping("/api/tests")
public class TestController {
    
    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    /**
     * Genera un nuevo test usando filtros opcionales.
     * 
     * Parámetros opcionales:
     * - tematica: Filtra por temática específica
     * - tipoPregunta: Filtra por tipo (VERDADERO_FALSO, UNICA, MULTIPLE)
     * - limite: Número de preguntas (por defecto 10)
     * 
     * @param tematica Filtro de temática (opcional)
     * @param tipoPregunta Filtro de tipo de pregunta (opcional)
     * @param limite Número máximo de preguntas (opcional, default 10)
     * @return TestPlayDTO con las preguntas del test
     */
    @GetMapping
    public ResponseEntity<TestPlayDTO> generarTest(
            @RequestParam(required = false) String tematica,
            @RequestParam(required = false) String tipoPregunta,
            @RequestParam(required = false, defaultValue = "10") Integer limite) {
        
        TestPlayDTO test = testService.generarTest(tematica, tipoPregunta, limite);
        return ResponseEntity.ok(test);
    }

    /**
     * Corrige un test enviado por el usuario.
     * 
     * - Valida las respuestas en memoria
     * - Calcula la puntuación
     * - Guarda solo el resumen del resultado (no las respuestas)
     * - Devuelve corrección detallada con explicaciones
     * 
     * Requiere que el usuario esté autenticado.
     * 
     * @param submitDTO Las respuestas del usuario
     * @param tematica Temática del test (para estadísticas)
     * @param tipoPregunta Tipo de pregunta del test (para estadísticas)
     * @return TestResultDTO con la corrección y puntuación
     */
    @PostMapping("/submit")
    public ResponseEntity<TestResultDTO> corregirTest(
            @Valid @RequestBody TestSubmitDTO submitDTO,
            @RequestParam(required = false) String tematica,
            @RequestParam(required = false) String tipoPregunta) {
        
        // Obtener el usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        TestResultDTO resultado = testService.corregirYGuardarTest(
                submitDTO, 
                username, 
                tematica, 
                tipoPregunta
        );
        
        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }

    @PostMapping("/submit/test")
public ResponseEntity<TestResultDTO> corregirTestSoloParaPruebas(
        @Valid @RequestBody TestSubmitDTO submitDTO) {
    
    TestResultDTO resultado = testService.corregirTestSinGuardar(submitDTO);
    return ResponseEntity.ok(resultado);
}
}
