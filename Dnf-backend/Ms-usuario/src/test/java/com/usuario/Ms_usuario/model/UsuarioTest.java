package com.usuario.Ms_usuario.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UsuarioTest {

    @Test
    void testGettersYSetters() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setName("Bayron");
        usuario.setEmail("bayron@gmail.com");
        usuario.setPassword("123456");
        usuario.setRol("ciudadano");
        usuario.setAvatar("url_avatar");

        assertEquals(1L, usuario.getId());
        assertEquals("Bayron", usuario.getName());
        assertEquals("bayron@gmail.com", usuario.getEmail());
        assertEquals("123456", usuario.getPassword());
        assertEquals("ciudadano", usuario.getRol());
        assertEquals("url_avatar", usuario.getAvatar());
    }

    @Test
    void testConstructorConArgumentos() {
        Usuario usuario = new Usuario(2L, "Juan", "juan@gmail.com", "pass", "admin", "avatar.png");

        assertEquals(2L, usuario.getId());
        assertEquals("Juan", usuario.getName());
        assertEquals("juan@gmail.com", usuario.getEmail());
    }
}