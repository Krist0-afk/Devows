package com.usuario.Ms_usuario.dto;

/**
 * DTO seguro para respuestas de usuario.
 * No incluye contraseña ni datos sensibles.
 */
public record UsuarioDTO(
    Long id,
    String name,
    String email,
    String rol,
    String avatar
) {}
