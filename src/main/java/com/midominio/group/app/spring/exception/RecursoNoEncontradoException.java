package com.midominio.group.app.spring.exception;

	/**
	 * Excepción que se lanza cuando no se encuentra un recurso solicitado.
	 * 
	 * GENÉRICA - Usada en todos los services de la aplicación:
	 * - PreguntaVerdaderoFalsoService
	 * - PreguntaSeleccionUnicaService (futuro)
	 * - PreguntaSeleccionMultipleService (futuro)
	 * - UsuarioService
	 * - TematicaService
	 * - TestService
	 * 
	 * Manejada por GlobalExceptionHandler para devolver HTTP 404
	 */
	public class RecursoNoEncontradoException extends RuntimeException {
	    	    
		private static final long serialVersionUID = 1L;
		private final String nombreRecurso;  // "Pregunta", "Usuario", "Test", etc.
	    private final Object valorClave;      // ID o identificador único
	    
	    /**
	     * Constructor principal
	     * 
	     * @param nombreRecurso tipo de recurso no encontrado (ej: "Pregunta", "Usuario")
	     * @param valorClave valor del identificador buscado (ej: ID)
	     * @param mensaje mensaje descriptivo del error
	     */
	    public RecursoNoEncontradoException(String nombreRecurso, Object valorClave, String mensaje) {
	        super(mensaje);
	        this.nombreRecurso = nombreRecurso;
	        this.valorClave = valorClave;
	    }
	    
	    /**
	     * Constructor simplificado con mensaje automático
	     */
	    public RecursoNoEncontradoException(String nombreRecurso, Object valorClave) {
	        this(nombreRecurso, valorClave, 
	             String.format("%s no encontrado con identificador: %s", nombreRecurso, valorClave));
	    }
	    
	    public String getNombreRecurso() {
	        return nombreRecurso;
	    }
	    
	    public Object getValorClave() {
	        return valorClave;
	    }
	}