package com.dcknotfnd.Ms_mascota.exception;

/**
 * Excepción lanzada cuando hay un conflicto (e.g., email duplicado).
 */
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }

    public ConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}
