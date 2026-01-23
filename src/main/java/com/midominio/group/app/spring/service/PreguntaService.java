package com.midominio.group.app.spring.service;


import com.midominio.group.app.spring.exception.RecursoNoEncontradoException;
import com.midominio.group.app.spring.exception.DatosInvalidosException;
import com.midominio.group.app.spring.entity.Pregunta;
import com.midominio.group.app.spring.repository.PreguntaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service genérico para todas las preguntas (polimórfico).
 * Gestiona operaciones comunes independientes del tipo de pregunta.
 * 
 * Usado en:
 * - Controllers de administración para listar/filtrar todas las preguntas
 * - Operaciones de eliminación masiva
 * - Búsqueda y filtrado por temática
 * - Listados paginados en el panel de administración
 * 
 * Para operaciones específicas de cada tipo usar:
 * - PreguntaVerdaderoFalsoService
 * - PreguntaSeleccionUnicaService (futuro)
 * - PreguntaSeleccionMultipleService (futuro)
 * 
 * Excepciones lanzadas:
 * - RecursoNoEncontradoException: cuando no existe una pregunta con el ID solicitado
 * - DatosInvalidosException: cuando los parámetros de búsqueda son inválidos
 */
@Service
@Transactional
public class PreguntaService {
    
    @Autowired
    private PreguntaRepository repository;
    
    // Constantes de validación
    private static final List<String> TIPOS_PERMITIDOS = List.of("VerdaderoFalso", "SeleccionUnica", "SeleccionMultiple");
    private static final int MAX_PAGE_SIZE = 100;
    
    /**
     * Obtiene todas las preguntas con paginación
     * 
     * Usado en:
     * - Panel de administración para listar todas las preguntas
     * - API REST GET /api/preguntas?page=0&size=10
     * 
     * @param pageable configuración de paginación (page, size, sort)
     * @return página de preguntas
     * @throws DatosInvalidosException si la configuración de paginación es inválida
     */
    public Page<Pregunta> listarTodas(Pageable pageable) {
        validarPaginacion(pageable);
        return repository.findAll(pageable);
    }
    
    /**
     * Obtiene todas las preguntas activas con paginación
     * 
     * Usado en:
     * - Generación de tests (solo se usan preguntas activas)
     * - Listado público de preguntas disponibles
     * 
     * @param pageable configuración de paginación
     * @return página de preguntas activas
     * @throws DatosInvalidosException si la configuración de paginación es inválida
     */
    public Page<Pregunta> listarActivas(Pageable pageable) {
        // Validación preventiva de paginación
        validarPaginacion(pageable);
        
        return repository.findByActivaTrue(pageable);
    }
    
    /**
     * Filtra preguntas por temática con paginación
     * 
     * Usado en:
     * - Búsqueda en panel de administración
     * - Generación de tests temáticos
     * - API REST GET /api/preguntas?tematica=Java&page=0
     * 
     * @param tematica temática a buscar
     * @param pageable configuración de paginación
     * @return página de preguntas filtradas
     * @throws DatosInvalidosException si los parámetros son inválidos
     */
    public Page<Pregunta> filtrarPorTematica(String tematica, Pageable pageable) {
        validarTematica(tematica);
        validarPaginacion(pageable);
        return repository.findByTematicaContainingIgnoreCase(tematica, pageable);
    }
    
    /**
     * Filtra preguntas por tipo (discriminator) con paginación
     * 
     * Usado en:
     * - Filtrar solo preguntas de Verdadero/Falso
     * - Filtrar solo preguntas de Selección Única (futuro)
     * - API REST GET /api/preguntas?tipo=VerdaderoFalso
     * 
     * @param tipo tipo de pregunta (ej: "VerdaderoFalso", "SeleccionUnica")
     * @param pageable configuración de paginación
     * @return página de preguntas del tipo especificado
     * @throws DatosInvalidosException si los parámetros son inválidos
     */
    public Page<Pregunta> filtrarPorTipo(String tipo, Pageable pageable) {
        validarTipo(tipo);
        validarPaginacion(pageable);
        return repository.findByDtype(tipo, pageable);
    }
    
    /**
     * Filtra preguntas por temática y estado (activa/inactiva) con paginación
     * 
     * Usado en:
     * - Búsqueda avanzada en panel de administración
     * - Generación de tests con filtros múltiples
     * 
     * @param tematica temática a buscar (puede ser null para no filtrar)
     * @param activa true para activas, false para inactivas, null para todas
     * @param pageable configuración de paginación
     * @return página de preguntas filtradas
     * @throws DatosInvalidosException si la paginación es inválida
     */
    public Page<Pregunta> filtrarPorTematicaYEstado(String tematica, Boolean activa, Pageable pageable) {
        validarPaginacion(pageable);
        
        // Si no se especifica temática ni estado, devolver todas
        if (esTematicaVacia(tematica) && activa == null) {
            return repository.findAll(pageable);
        }
        
        // Si solo se especifica estado
        if (esTematicaVacia(tematica)) {
            return activa ? repository.findByActivaTrue(pageable) 
                         : repository.findByActivaFalse(pageable);
        }
        
        // Si solo se especifica temática
        if (activa == null) {
            return repository.findByTematicaContainingIgnoreCase(tematica, pageable);
        }
        
        // Si se especifican ambos filtros
        return repository.findByTematicaContainingIgnoreCaseAndActiva(tematica, activa, pageable);
    }
    
    /**
     * Obtiene una pregunta por ID (polimórfico)
     * 
     * Usado en:
     * - Vistas de detalle de pregunta
     * - Validación de existencia antes de operaciones
     * 
     * @param id identificador de la pregunta
     * @return la pregunta encontrada (puede ser de cualquier tipo)
     * @throws RecursoNoEncontradoException si no existe
     * @throws DatosInvalidosException si el ID es inválido
     */
    public Pregunta obtenerPorId(Long id) {
        validarIdPositivo(id);
        return repository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Pregunta", id));
    }
    
    /**
     * Elimina una pregunta por ID
     * 
     * Usado en:
     * - Panel de administración al eliminar preguntas
     * - API REST DELETE /api/preguntas/{id}
     * 
     * @param id identificador de la pregunta a eliminar
     * @throws RecursoNoEncontradoException si no existe la pregunta
     * @throws DatosInvalidosException si el ID es inválido
     */
    public void eliminar(Long id) {
        validarIdPositivo(id);
        
        if (!repository.existsById(id)) {
            throw new RecursoNoEncontradoException("Pregunta", id);
        }
        
        repository.deleteById(id);
    }
    
    /**
     * Activa o desactiva una pregunta
     * 
     * Usado en:
     * - Panel de administración para activar/desactivar preguntas
     * - API REST PATCH /api/preguntas/{id}/estado
     * 
     * @param id identificador de la pregunta
     * @param activa nuevo estado (true = activa, false = inactiva)
     * @return la pregunta con el estado actualizado
     * @throws RecursoNoEncontradoException si no existe
     * @throws DatosInvalidosException si los parámetros son inválidos
     */
    public Pregunta cambiarEstado(Long id, Boolean activa) {
        validarIdPositivo(id);
        
        if (activa == null) {
            throw new DatosInvalidosException("activa", "El estado no puede ser nulo");
        }
        
        Pregunta pregunta = repository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Pregunta", id));
        
        pregunta.setActiva(activa);
        return repository.save(pregunta);
    }
    
    /**
     * Obtiene el total de preguntas en la base de datos
     * 
     * Usado en:
     * - Dashboard de administración para estadísticas
     * - Verificación antes de generar tests
     * 
     * @return número total de preguntas
     */
    public long contarTotal() {
        return repository.count();
    }
    
    /**
     * Obtiene el total de preguntas activas
     * 
     * Usado en:
     * - Dashboard para mostrar preguntas disponibles
     * - Validación antes de generar tests
     * 
     * @return número de preguntas activas
     */
    public long contarActivas() {
        return repository.countByActivaTrue();
    }
    
    /**
     * Obtiene lista de todas las temáticas distintas en la base de datos
     * 
     * Usado en:
     * - Filtros de búsqueda (dropdown de temáticas)
     * - Generación de tests por temática
     * 
     * @return lista de temáticas únicas
     */
    public List<String> obtenerTematicasDisponibles() {
        // Definido en: PreguntaRepository.findDistinctTematicas()
        List<String> tematicas = repository.findDistinctTematicas();
        
        // Validar que existan temáticas
        if (tematicas == null || tematicas.isEmpty()) {
            // No lanzar excepción, simplemente devolver lista vacía
            return List.of();
        }
        
        return tematicas;
    }
    
    /**
     * Método privado para validar configuración de paginación
     * Centraliza validaciones para evitar código duplicado
     * 
     * Llamado desde:
     * - listarTodas()
     * - listarActivas()
     * - filtrarPorTematica()
     * - filtrarPorTipo()
     * - filtrarPorTematicaYEstado()
     * 
     * @param pageable configuración a validar
     * @throws DatosInvalidosException si la configuración es inválida
     */
    private void validarPaginacion(Pageable pageable) {
        if (pageable == null) {
            throw new DatosInvalidosException("pageable", "La configuración de paginación no puede ser nula");
        }
        
        if (pageable.getPageNumber() < 0) {
            throw new DatosInvalidosException("page", "El número de página no puede ser negativo");
        }
        
        if (pageable.getPageSize() <= 0) {
            throw new DatosInvalidosException("size", "El tamaño de página debe ser mayor a 0");
        }
        
        if (pageable.getPageSize() > MAX_PAGE_SIZE) {
            throw new DatosInvalidosException("size", "El tamaño de página no puede exceder " + MAX_PAGE_SIZE + " elementos");
        }
    }
    
    /**
     * Método privado para validar que un ID sea positivo
     * Evita duplicación de validación en obtenerPorId(), eliminar() y cambiarEstado()
     * 
     * @param id identificador a validar
     * @throws DatosInvalidosException si el ID es nulo o menor/igual a 0
     */
    private void validarIdPositivo(Long id) {
        if (id == null || id <= 0) {
            throw new DatosInvalidosException("id", "El ID debe ser un número positivo");
        }
    }
    
    /**
     * Método privado para validar que una temática no esté vacía
     * Evita duplicación de validación en filtrarPorTematica()
     * 
     * @param tematica temática a validar
     * @throws DatosInvalidosException si la temática es nula o vacía
     */
    private void validarTematica(String tematica) {
        if (tematica == null || tematica.trim().isEmpty()) {
            throw new DatosInvalidosException("tematica", "La temática no puede estar vacía");
        }
    }
    
    /**
     * Método privado para validar que una temática esté vacía
     * Utilizado en filtrarPorTematicaYEstado() para condiciones lógicas
     * 
     * @param tematica temática a evaluar
     * @return true si la temática es nula o está vacía, false en caso contrario
     */
    private boolean esTematicaVacia(String tematica) {
        return tematica == null || tematica.trim().isEmpty();
    }
    
    /**
     * Método privado para validar que un tipo sea permitido
     * Evita duplicación de validación en filtrarPorTipo()
     * 
     * @param tipo tipo de pregunta a validar
     * @throws DatosInvalidosException si el tipo es nulo, vacío o no permitido
     */
    private void validarTipo(String tipo) {
        if (tipo == null || tipo.trim().isEmpty()) {
            throw new DatosInvalidosException("tipo", "El tipo de pregunta no puede estar vacío");
        }
        
        if (!TIPOS_PERMITIDOS.contains(tipo)) {
            throw new DatosInvalidosException(
                "tipo",
                "Tipo de pregunta inválido. Valores permitidos: " + String.join(", ", TIPOS_PERMITIDOS)
            );
        }
    }
}