package com.usuario.Ms_usuario.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad que registra los intentos de login (exitosos y fallidos).
 * Se utiliza para implementar el mecanismo de bloqueo de cuenta por intentos fallidos.
 * 
 * Campos:
 * - id: Identificador único del registro
 * - usuarioId: ID del usuario que intenta login
 * - intentoExitoso: true si el login fue exitoso, false si falló
 * - fechaIntento: Fecha y hora del intento
 * - direccionIp: Dirección IP desde donde se realizó el intento
 * - razonFallo: Motivo del fallo (si aplica)
 */
@Entity
@Table(name = "login_attempts", indexes = {
    @Index(name = "idx_usuario_id", columnList = "usuario_id"),
    @Index(name = "idx_fecha_intento", columnList = "fecha_intento")
})
public class LoginAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(nullable = false)
    private boolean intentoExitoso = false;

    @Column(name = "fecha_intento", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime fechaIntento;

    @Column(nullable = true, length = 45)
    private String direccionIp;

    @Column(nullable = true, length = 255)
    private String razonFallo;

    public LoginAttempt() {
    }

    public LoginAttempt(Long usuarioId, boolean intentoExitoso, LocalDateTime fechaIntento, String direccionIp, String razonFallo) {
        this.usuarioId = usuarioId;
        this.intentoExitoso = intentoExitoso;
        this.fechaIntento = fechaIntento;
        this.direccionIp = direccionIp;
        this.razonFallo = razonFallo;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public boolean isIntentoExitoso() {
        return intentoExitoso;
    }

    public void setIntentoExitoso(boolean intentoExitoso) {
        this.intentoExitoso = intentoExitoso;
    }

    public LocalDateTime getFechaIntento() {
        return fechaIntento;
    }

    public void setFechaIntento(LocalDateTime fechaIntento) {
        this.fechaIntento = fechaIntento;
    }

    public String getDireccionIp() {
        return direccionIp;
    }

    public void setDireccionIp(String direccionIp) {
        this.direccionIp = direccionIp;
    }

    public String getRazonFallo() {
        return razonFallo;
    }

    public void setRazonFallo(String razonFallo) {
        this.razonFallo = razonFallo;
    }
}
