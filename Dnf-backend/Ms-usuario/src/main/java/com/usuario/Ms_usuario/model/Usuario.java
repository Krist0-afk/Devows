package com.usuario.Ms_usuario.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Entidad que representa un usuario en el sistema.
 * Almacena información personal, autenticación y roles.
 */
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio y no puede estar vacío")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ingresar un formato de correo electrónico válido")
    @Column(nullable = false, unique = true)
    private String email;

    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    @Column(nullable = false)
    private String password;

    /**
     * Rol del usuario en el sistema.
     * Puede ser: USER, ADMIN, MODERATOR, etc.
     */
    @Column(nullable = false)
    private String rol;

    @Column(nullable = true)
    private String avatar;

    /**
     * Indica si la cuenta está activa.
     * false = cuenta desactivada/bloqueada
     */
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
    private boolean activo = true;

    /**
     * Número de intentos fallidos de login.
     * Se reinicia después de un login exitoso.
     * Se usa para implementar bloqueo temporal.
     */
    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private int intentosFallidos = 0;

    public Usuario() {
    }

    public Usuario(Long id, String name, String email, String password, String rol, String avatar) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.rol = rol;
        this.avatar = avatar;
        this.activo = true;
        this.intentosFallidos = 0;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public int getIntentosFallidos() {
        return intentosFallidos;
    }

    public void setIntentosFallidos(int intentosFallidos) {
        this.intentosFallidos = intentosFallidos;
    }

    /**
     * Incrementa el contador de intentos fallidos.
     */
    public void incrementarIntentosFallidos() {
        this.intentosFallidos++;
    }

    /**
     * Reinicia el contador de intentos fallidos.
     */
    public void reiniciarIntentosFallidos() {
        this.intentosFallidos = 0;
    }
}
