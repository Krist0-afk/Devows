package com.usuario.Ms_usuario.model;

/**
 * Enum que define los roles disponibles en el sistema.
 * Los roles determinan los permisos y acceso a recursos.
 */
public enum Rol {
    /**
     * Usuario regular con permisos limitados.
     */
    USER("ROLE_USER", "Usuario regular"),

    /**
     * Administrador con permisos completos.
     */
    ADMIN("ROLE_ADMIN", "Administrador del sistema"),

    /**
     * Moderador con permisos intermedios.
     */
    MODERATOR("ROLE_MODERATOR", "Moderador de contenido");

    private final String authority;
    private final String descripcion;

    Rol(String authority, String descripcion) {
        this.authority = authority;
        this.descripcion = descripcion;
    }

    public String getAuthority() {
        return authority;
    }

    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Obtiene el rol desde su nombre (case-insensitive).
     * 
     * @param nombre Nombre del rol
     * @return Rol encontrado
     * @throws IllegalArgumentException si el rol no existe
     */
    public static Rol fromNombre(String nombre) {
        try {
            return Rol.valueOf(nombre.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Rol desconocido: " + nombre);
        }
    }
}
