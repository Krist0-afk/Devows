package com.notificaciones.Ms_notificaciones.dto;

import java.time.LocalDateTime;

/**
 * Respuesta genérica estándar para todos los endpoints.
 * Proporciona un formato consistente para todas las respuestas de la API.
 */
public class ApiResponse<T> {
    private boolean success;
    private int code;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    public ApiResponse(boolean success, int code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    public ApiResponse(boolean success, int code, String message) {
        this(success, code, message, null);
    }

    // Métodos estáticos helper
    public static <T> ApiResponse<T> success(int code, String message, T data) {
        return new ApiResponse<>(true, code, message, data);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, 200, message, data);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, 200, "Operación exitosa", data);
    }

    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>(false, code, message, null);
    }

    // Getters y Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
