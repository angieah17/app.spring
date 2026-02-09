package com.midominio.group.app.spring.controller;

import com.midominio.group.app.spring.dto.TestPlayDTO;
import com.midominio.group.app.spring.dto.TestResultDTO;
import com.midominio.group.app.spring.dto.TestSubmitDTO;
import com.midominio.group.app.spring.entity.ResultadoTest;
import com.midominio.group.app.spring.entity.Usuario;
import com.midominio.group.app.spring.service.TestService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para tests de usuario final.
 * 
 * Acceso: Usuarios autenticados (role = "user" o "admin")
 * Responsabilidades:
 * - Generar tests interactivos
 * - Corregir respuestas en memoria (sin persistir cada respuesta)
 * - Guardar historial de resultados
 * - Consultar histórico de tests
 * 
 * Base URL: /api/test
 */
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/test")
public class TestController {
    
    private final TestService testService;
    
    public TestController(TestService testService) {
        this.testService = testService;
    }
    
    /**
     * Genera un test interactivo con preguntas filtradas.
     * Las respuestas correctas NO se incluyen en la respuesta.
     * 
     * @param tematica La temática del test (requerida)
     * @param tipoPregunta Tipo: VERDADERO_FALSO, UNICA, MULTIPLE (opcional)
     * @param cantidad Número de preguntas (opcional, default 10)
     * @return TestPlayDTO con preguntas sin respuestas correctas
     * 
     * Ejemplos:
     * GET /api/test/Historia
     * GET /api/test/Geografía?cantidad=15
     * GET /api/test/Matemáticas?tipoPregunta=UNICA&cantidad=8
     */
    @GetMapping("/{tematica}")
    public ResponseEntity<TestPlayDTO> generarTest(
            @PathVariable String tematica,
            @RequestParam(required = false) String tipoPregunta,
            @RequestParam(required = false) Integer cantidad) {
        
        TestPlayDTO test = testService.generarTest(tematica, tipoPregunta, cantidad);
        return ResponseEntity.ok(test);
    }
    
    /**
     * Envía las respuestas de un test para corrección.
     * 
     * Proceso:
     * 1. Recibe las respuestas del usuario
     * 2. Corrige en memoria (compara con respuestas correctas)
     * 3. Calcula puntuación
     * 4. Guarda resumen en BD (sin persistir cada respuesta)
     * 5. Devuelve resultado con revisión detallada y explicaciones
     * 
     * Request Body:
     * {
     *     "testId": "uuid-generado-en-GET",
     *     "tematica": "Historia",
     *     "respuestas": {
     *         1: true,                    // Pregunta VF
     *         2: 1,                       // Pregunta Única (índice)
     *         3: [0, 2]                   // Pregunta Múltiple (índices)
     *     }
     * }
     * 
     * @param submit DTO con respuestas del usuario
     * @return TestResultDTO con puntuación, revisión y explicaciones
     */
    @PostMapping("/submit")
    public ResponseEntity<TestResultDTO> enviarTest(@Valid @RequestBody TestSubmitDTO submit) {
        // TODO: Obtener usuario autenticado del contexto de seguridad
        // Usar SecurityContextHolder.getContext().getAuthentication().getPrincipal()
        Usuario usuarioAutenticado = new Usuario("usuario_demo", "password", "user");
        usuarioAutenticado.setId(1L);
        
        TestResultDTO resultado = testService.corregirTest(submit, usuarioAutenticado);
        return ResponseEntity.ok(resultado);
    }
    
    /**
     * Obtiene el historial completo de tests del usuario autenticado.
     * 
     * @return Lista de todos los tests realizados con sus puntuaciones
     */
    @GetMapping("/historial")
    public ResponseEntity<List<ResultadoTest>> obtenerHistorial() {
        // TODO: Obtener usuario autenticado del contexto de seguridad
        Usuario usuarioAutenticado = new Usuario("usuario_demo", "password", "user");
        usuarioAutenticado.setId(1L);
        
        List<ResultadoTest> historial = testService.obtenerHistorialUsuario(usuarioAutenticado);
        return ResponseEntity.ok(historial);
    }
    
    /**
     * Obtiene el historial de tests de una temática específica.
     * 
     * @param tematica La temática a filtrar
     * @return Lista de tests realizados en esa temática
     */
    @GetMapping("/historial/{tematica}")
    public ResponseEntity<List<ResultadoTest>> obtenerHistorialPorTematica(
            @PathVariable String tematica) {
        // TODO: Obtener usuario autenticado del contexto de seguridad
        Usuario usuarioAutenticado = new Usuario("usuario_demo", "password", "user");
        usuarioAutenticado.setId(1L);
        
        List<ResultadoTest> historial = testService.obtenerHistorialPorTematica(usuarioAutenticado, tematica);
        return ResponseEntity.ok(historial);
    }
    
    /**
     * Obtiene estadísticas del usuario (próximamente).
     * - Puntuación promedio por temática
     * - Total de tests realizados
     * - Temática favorita
     * - Progreso en el tiempo
     */
    @GetMapping("/estadisticas")
    public ResponseEntity<String> obtenerEstadisticas() {
        return ResponseEntity.ok("Endpoint en desarrollo: estadísticas del usuario");
    }
}
