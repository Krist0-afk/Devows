package com.usuario.Ms_usuario.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DtoTest {

    @Test
    void testAuthRequest() {
        AuthRequest req = new AuthRequest("test@gmail.com", "secreta");
        assertEquals("test@gmail.com", req.email());
        assertEquals("secreta", req.password());
    }

    @Test
    void testAuthResponse() {
        UsuarioDTO usuario = new UsuarioDTO(1L, "Prueba", "test@test.com", "USER", null);
        AuthResponse res = new AuthResponse("token123", usuario);

        assertEquals("token123", res.token());
        assertEquals("Prueba", res.user().name());
    }
}