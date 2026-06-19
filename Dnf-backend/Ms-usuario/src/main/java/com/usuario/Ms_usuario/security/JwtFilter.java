package com.usuario.Ms_usuario.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Extraer la cabecera Authorization
        final String authHeader = request.getHeader("Authorization");

        // 2. Si no hay cabecera o no empieza con "Bearer ", ignoramos y pasamos al siguiente filtro
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extraer el token (quitando los primeros 7 caracteres de "Bearer ")
        final String jwt = authHeader.substring(7);

        try {
            // 4. Extraer el correo del token
            String userEmail = jwtUtil.extractUsername(jwt);

            // 5. Si hay un correo y el usuario aún no está autenticado en este hilo
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // 6. Validamos criptográficamente el token
                if (jwtUtil.isTokenValid(jwt)) {
                    // Creamos el objeto de autenticación de Spring (Sin roles por ahora: new ArrayList<>())
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userEmail, null, new ArrayList<>());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Le decimos a Spring: "Este usuario es válido, déjalo pasar"
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // Si el token es inválido o expirado, no hacemos nada.
            // Spring Security automáticamente retornará 403 Forbidden al llegar al controlador.
        }

        // 7. Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }
}