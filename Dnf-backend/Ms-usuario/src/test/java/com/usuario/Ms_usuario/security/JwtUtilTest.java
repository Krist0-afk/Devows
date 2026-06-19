package com.usuario.Ms_usuario.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil("EstaEsUnaClaveSecretaMuyLargaParaAsegurarElTokenDeSanosYSalvos2026");
    }

    @Test
    void testGenerarYExtraerToken() {
        String email = "admin@gmail.com";
        String token = jwtUtil.generateToken(email);

        assertNotNull(token);
        assertEquals(email, jwtUtil.extractUsername(token));
        assertTrue(jwtUtil.isTokenValid(token));
    }

    @Test
    void testTokenInvalido() {
        String tokenFalso = "este.esun.tokenfalso";
        assertFalse(jwtUtil.isTokenValid(tokenFalso));
    }
}