package com.dcknotfnd.Ms_mascota.exception;

/**
 * Excepción lanzada cuando el acceso no está autorizado.
 */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}
