package com.midominio.group.app.spring.exception;

/**
 * Excepción que se lanza cuando los datos de entrada no son válidos.
 * 
 * GENÉRICA - Usada para validaciones en todos los services:
 * - Validaciones de campos obligatorios
 * - Validaciones de formato
 * - Validaciones de rangos de valores
 * - Validaciones de reglas de negocio
 * 
 * Manejada por GlobalExceptionHandler para devolver HTTP 400
 * 
 * Nota: Se podría usar la IllegalArgumentException estándar de Java,
 * pero esta personalizada permite agregar más contexto si es necesario.
 */

public class DatosInvalidosException extends RuntimeException {
    
	private static final long serialVersionUID = 1L;
	
	private final String campo;  // Campo que tiene el error (opcional)
    
    /**
     * Constructor con campo específico
     */
    public DatosInvalidosException(String campo, String mensaje) {
        super(mensaje);
        this.campo = campo;
    }
    
    /**
     * Constructor simplificado
     */
    public DatosInvalidosException(String mensaje) {
        super(mensaje);
        this.campo = null;
    }
    
    public String getCampo() {
        return campo;
    }
}