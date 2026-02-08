package com.midominio.group.app.spring.exception;

public class InsufficientQuestionsException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public InsufficientQuestionsException(String message) {
        super(message);
    }
}
