package com.usuario.Ms_usuario.exception;

/**
 * Excepción lanzada cuando un usuario intenta login pero su cuenta está bloqueada.
 */
public class AccountLockedException extends RuntimeException {
    private long minutosRestantes;

    public AccountLockedException(String message) {
        super(message);
        this.minutosRestantes = 0;
    }

    public AccountLockedException(String message, long minutosRestantes) {
        super(message);
        this.minutosRestantes = minutosRestantes;
    }

    public long getMinutosRestantes() {
        return minutosRestantes;
    }

    public void setMinutosRestantes(long minutosRestantes) {
        this.minutosRestantes = minutosRestantes;
    }
}
