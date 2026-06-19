package com.usuario.Ms_usuario.dto;

import java.time.LocalDateTime;

/**
 * DTO para mostrar historial de intentos de login.
 * Proporciona información de auditoría sobre los intentos de login del usuario.
 */
public record LoginAuditDTO(
    Long id,
    Long usuarioId,
    boolean intentoExitoso,
    LocalDateTime fechaIntento,
    String direccionIp,
    String razonFallo
) {
}
