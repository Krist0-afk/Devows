package com.usuario.Ms_usuario.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class JwtFilterTest {

    @InjectMocks
    private JwtFilter jwtFilter;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext(); // Limpiamos la seguridad antes de cada test
    }

    @Test
    void testFiltroConTokenValido() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer tokenValido123");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jwtUtil.extractUsername("tokenValido123")).thenReturn("test@gmail.com");
        when(jwtUtil.isTokenValid("tokenValido123")).thenReturn(true);

        jwtFilter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testFiltroSinHeader() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest(); // Sin header
        MockHttpServletResponse response = new MockHttpServletResponse();

        jwtFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testFiltroConTokenInvalidoLanzaExcepcion() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer tokenFalso");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jwtUtil.extractUsername("tokenFalso")).thenThrow(new RuntimeException("Token alterado"));

        jwtFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testFiltroHeaderExistePeroNoEsBearer() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Basic dXN1YXJpbzpwYXNzd29yZA==");
        MockHttpServletResponse response = new MockHttpServletResponse();

        jwtFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testFiltroTokenValidoPeroYaAutenticado() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer tokenCualquiera");
        MockHttpServletResponse response = new MockHttpServletResponse();

        SecurityContextHolder.getContext().setAuthentication(
                new org.springframework.security.authentication.UsernamePasswordAuthenticationToken("previo@gmail.com", null, new java.util.ArrayList<>())
        );

        when(jwtUtil.extractUsername("tokenCualquiera")).thenReturn("nuevo@gmail.com");

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(jwtUtil, never()).isTokenValid(anyString());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testFiltroExtraeEmailPeroTokenEstaExpirado() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer tokenExpirado");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jwtUtil.extractUsername("tokenExpirado")).thenReturn("test@gmail.com");
        when(jwtUtil.isTokenValid("tokenExpirado")).thenReturn(false);

        jwtFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }
}