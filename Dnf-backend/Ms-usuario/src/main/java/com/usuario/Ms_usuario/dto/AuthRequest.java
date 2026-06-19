package com.usuario.Ms_usuario.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para solicitudes de login.
 * Contiene email y contraseña con validaciones básicas.
 */
public record AuthRequest(
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    String email,
    
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    String password
) {}
