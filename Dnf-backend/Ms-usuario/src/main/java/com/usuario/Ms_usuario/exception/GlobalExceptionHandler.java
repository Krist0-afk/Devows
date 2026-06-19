package com.usuario.Ms_usuario.exception;

import com.usuario.Ms_usuario.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.stream.Collectors;

/**
 * Manejador global de excepciones para la aplicación.
 * Proporciona respuestas consistentes para todos los errores.
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Maneja excepciones de validación de entrada.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse<?>> handleValidationException(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.joining(", "));

        logger.warn("Validación fallida: {}", errors);
        return ResponseEntity.badRequest()
            .body(ApiResponse.error(400, "Validación fallida: " + errors));
    }

    /**
     * Maneja excepciones de usuario personalizadas.
     */
    @ExceptionHandler(UsuarioException.class)
    public ResponseEntity<ApiResponse<?>> handleUsuarioException(UsuarioException ex) {
        logger.error("Error de usuario: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(400, ex.getMessage()));
    }

    /**
     * Maneja excepciones de acceso no autorizado.
     */
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ApiResponse<?>> handleUnauthorizedException(UnauthorizedException ex) {
        logger.warn("Acceso no autorizado: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(ApiResponse.error(401, ex.getMessage()));
    }

    /**
     * Maneja excepciones de recurso no encontrado.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiResponse<?>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        logger.warn("Recurso no encontrado: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ApiResponse.error(404, ex.getMessage()));
    }

    /**
     * Maneja excepciones de conflicto (e.g., email duplicado).
     */
    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ApiResponse<?>> handleConflictException(ConflictException ex) {
        logger.warn("Conflicto: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(ApiResponse.error(409, ex.getMessage()));
    }

    /**
     * Maneja excepciones de cuenta bloqueada por intentos fallidos.
     */
    @ExceptionHandler(AccountLockedException.class)
    @ResponseStatus(HttpStatus.LOCKED)
    public ResponseEntity<ApiResponse<?>> handleAccountLockedException(AccountLockedException ex) {
        logger.warn("Cuenta bloqueada: {}", ex.getMessage());
        ApiResponse<?> response = ApiResponse.error(423, ex.getMessage());
        // Incluir tiempo restante en la respuesta si está disponible
        if (ex.getMinutosRestantes() > 0) {
            response.setMessage(ex.getMessage() + " (Desbloqueo disponible en " + ex.getMinutosRestantes() + " minutos)");
        }
        return ResponseEntity.status(HttpStatus.LOCKED)
            .body(response);
    }

    /**
     * Maneja todas las excepciones no capturadas.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiResponse<?>> handleGlobalException(Exception ex) {
        logger.error("Error interno del servidor: ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.error(500, "Error interno del servidor"));
    }
}
