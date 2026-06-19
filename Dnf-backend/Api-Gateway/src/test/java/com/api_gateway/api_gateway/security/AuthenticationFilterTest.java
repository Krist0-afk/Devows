package com.api_gateway.api_gateway.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AuthenticationFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private AuthenticationFilter authenticationFilter;

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));
    }

    @Test
    public void testPublicRoute_Login_Permitted() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/usuarios/login");
        when(request.getMethod()).thenReturn("POST");

        authenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verify(jwtUtil, never()).isTokenValid(anyString());
    }

    @Test
    public void testPublicRoute_Swagger_Permitted() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/swagger-ui/index.html");
        when(request.getMethod()).thenReturn("GET");

        authenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verify(jwtUtil, never()).isTokenValid(anyString());
    }

    @Test
    public void testProtectedRoute_NoAuthHeader_Unauthorized() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/mascotas");
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("Authorization")).thenReturn(null);

        authenticationFilter.doFilterInternal(request, response, filterChain);

        verify(response, times(1)).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    public void testProtectedRoute_InvalidAuthHeaderFormat_Unauthorized() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/mascotas");
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("Authorization")).thenReturn("TokenInvalido123");

        authenticationFilter.doFilterInternal(request, response, filterChain);

        verify(response, times(1)).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    public void testProtectedRoute_ValidToken_Permitted() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/mascotas");
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("Authorization")).thenReturn("Bearer tokenValidisimo123");
        when(jwtUtil.isTokenValid("tokenValidisimo123")).thenReturn(true);

        authenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verify(jwtUtil, times(1)).isTokenValid("tokenValidisimo123");
    }

    @Test
    public void testProtectedRoute_ExpiredOrInvalidToken_Unauthorized() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/mascotas");
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("Authorization")).thenReturn("Bearer tokenMalo456");
        when(jwtUtil.isTokenValid("tokenMalo456")).thenReturn(false);

        authenticationFilter.doFilterInternal(request, response, filterChain);

        verify(response, times(1)).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(filterChain, never()).doFilter(request, response);
    }
}
