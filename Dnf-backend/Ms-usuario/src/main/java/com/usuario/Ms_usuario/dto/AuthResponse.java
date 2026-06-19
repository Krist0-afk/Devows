package com.usuario.Ms_usuario.dto;

/**
 * DTO para respuestas de autenticación.
 * Retorna el token JWT y datos del usuario sin información sensible.
 */
public record AuthResponse(
    String token,
    UsuarioDTO user
) {}
