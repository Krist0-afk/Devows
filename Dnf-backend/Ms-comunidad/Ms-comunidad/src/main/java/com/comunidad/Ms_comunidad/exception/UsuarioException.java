package com.comunidad.Ms_comunidad.exception;

/**
 * Excepción personalizada para operaciones de usuario.
 */
public class UsuarioException extends RuntimeException {
    private final String code;

    public UsuarioException(String message, String code) {
        super(message);
        this.code = code;
    }

    public UsuarioException(String message) {
        super(message);
        this.code = "USUARIO_ERROR";
    }

    public String getCode() {
        return code;
    }
}
