package com.comunidad.Ms_comunidad.exception;

/**
 * Excepción lanzada cuando un recurso no es encontrado.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
