/*
 * ESTRUCTURA DE SERVICIOS REFACTORIZADA CON HERENCIA
 * 
 * Jerarquía de clases:
 * 
 *   AbstractPreguntaService<T>
 *   ├── Proporciona métodos genéricos para validación
 *   ├── Métodos protected para reutilización
 *   └── Interfaz abstracta getRepository()
 *      │
 *      ├── PreguntaVerdaderoFalsoService
 *      │   ├── crear(T)
 *      │   ├── actualizar(Long, T)
 *      │   ├── validarRespuesta(Long, Boolean)
 *      │   └── obtenerPorId(Long) [heredado]
 *      │
 *      ├── PreguntaSeleccionUnicaService [FUTURO]
 *      │   ├── crear(T)
 *      │   ├── actualizar(Long, T)
 *      │   ├── validarRespuesta(Long, String/Long)
 *      │   └── obtenerPorId(Long) [heredado]
 *      │
 *      └── PreguntaSeleccionMultipleService [FUTURO]
 *          ├── crear(T)
 *          ├── actualizar(Long, T)
 *          ├── validarRespuesta(Long, List<Long>)
 *          └── obtenerPorId(Long) [heredado]
 * 
 * BENEFICIOS DE LA ARQUITECTURA ACTUAL:
 * 
 * 1. HERENCIA (DRY - Don't Repeat Yourself)
 *    - Métodos comunes en AbstractPreguntaService
 *    - validarIdPositivo(), validarEnunciado(), validarTematica(), etc.
 *    - Evita duplicación entre servicios específicos
 * 
 * 2. EXTENSIBILIDAD
 *    - Nuevos servicios heredan toda la funcionalidad base
 *    - Solo implementan lógica específica del tipo
 *    - Cambios en validaciones se aplican automáticamente
 * 
 * 3. CONSISTENCIA
 *    - Todos los servicios validan de la misma manera
 *    - Límites de caracteres centralizados en constantes
 *    - Excepciones uniformes
 * 
 * 4. MANTENIBILIDAD
 *    - Cambiar un límite de caracteres: solo en AbstractPreguntaService
 *    - Añadir una validación nueva: reutilizable en todos los servicios
 *    - Código más limpio y enfocado
 * 
 * MÉTODOS HEREDADOS EN SUBCLASES:
 * 
 * protected void validarIdPositivo(Long id)
 * protected void validarNoNulo(Object o, String nombre)
 * protected void validarEnunciado(String enunciado)
 * protected void validarTematica(String tematica)
 * protected void validarExplicacion(String explicacion)
 * protected void validarRespuestaUsuario(Boolean respuesta)
 * protected void validarPreguntaActiva(T pregunta, Long id)
 * public T obtenerPorId(Long id)
 * 
 * CONSTANTES HEREDADAS EN SUBCLASES:
 * 
 * protected static final int ENUNCIADO_MIN_LENGTH = 10
 * protected static final int ENUNCIADO_MAX_LENGTH = 500
 * protected static final int TEMATICA_MAX_LENGTH = 100
 * protected static final int EXPLICACION_MAX_LENGTH = 1000
 * 
 * MÉTODO ABSTRACTO A IMPLEMENTAR:
 * 
 * @Override
 * protected JpaRepository<T, Long> getRepository() {
 *     return repository;
 * }
 * 
 * EJEMPLO DE IMPLEMENTACIÓN (PreguntaSeleccionUnicaService):
 * 
 * @Service
 * @Transactional
 * public class PreguntaSeleccionUnicaService 
 *     extends AbstractPreguntaService<PreguntaSeleccionUnica> {
 *     
 *     @Autowired
 *     private PreguntaSeleccionUnicaRepository repository;
 *     
 *     @Override
 *     protected JpaRepository<PreguntaSeleccionUnica, Long> getRepository() {
 *         return repository;
 *     }
 *     
 *     public PreguntaSeleccionUnica crear(PreguntaSeleccionUnica pregunta) {
 *         validarDatosPregunta(pregunta);
 *         // ... lógica específica
 *         return repository.save(pregunta);
 *     }
 *     
 *     private void validarDatosPregunta(PreguntaSeleccionUnica pregunta) {
 *         validarNoNulo(pregunta, "Pregunta");
 *         validarEnunciado(pregunta.getEnunciado());
 *         validarOpciones(pregunta.getOpciones());  // Específico
 *         validarTematica(pregunta.getTematica());
 *         validarExplicacion(pregunta.getExplicacion());
 *     }
 *     
 *     private void validarOpciones(List<String> opciones) {
 *         // Validación específica para opciones
 *     }
 * }
 * 
 * PASOS PARA CREAR UN NUEVO SERVICIO ESPECÍFICO:
 * 
 * 1. Crear clase que extienda AbstractPreguntaService<T>
 * 2. Inyectar el repositorio específico
 * 3. Implementar getRepository()
 * 4. Implementar crear() y actualizar() según el tipo
 * 5. Implementar validaciones específicas del tipo
 * 6. Reutilizar métodos heredados de la clase abstracta
 * 
 */
