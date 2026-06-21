package com.api_gateway.api_gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuración de seguridad del API Gateway.
 * Registra el AuthenticationFilter en la cadena de filtros de Spring Security.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private AuthenticationFilter authenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(authz -> authz
                // Permitir rutas públicas
                .requestMatchers(new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/api/pets/**", "GET")).permitAll()
                .requestMatchers(new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/api/pets", "GET")).permitAll()
                .requestMatchers(new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/api/success-stories/**", "GET")).permitAll()
                .requestMatchers(new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/api/success-stories", "GET")).permitAll()
                .requestMatchers(new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/api/usuarios/login")).permitAll()
                .requestMatchers(new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/api/usuarios")).permitAll()
                .requestMatchers(new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/swagger-ui/**")).permitAll()
                .requestMatchers(new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/v3/api-docs/**")).permitAll()
                .requestMatchers(new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/actuator/**")).permitAll()
                // Todas las demás rutas requieren autenticación
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // Registrar el AuthenticationFilter ANTES de UsernamePasswordAuthenticationFilter
            .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {
        org.springframework.web.cors.CorsConfiguration swaggerConfig = new org.springframework.web.cors.CorsConfiguration();
        swaggerConfig.setAllowedOriginPatterns(java.util.Arrays.asList("*"));
        swaggerConfig.setAllowedMethods(java.util.Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        swaggerConfig.setAllowedHeaders(java.util.Arrays.asList("*"));
        swaggerConfig.setAllowCredentials(false);

        org.springframework.web.cors.UrlBasedCorsConfigurationSource source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/v3/api-docs/**", swaggerConfig);
        source.registerCorsConfiguration("/swagger-ui/**", swaggerConfig);
        source.registerCorsConfiguration("/swagger-resources/**", swaggerConfig);
        source.registerCorsConfiguration("/swagger-ui.html", swaggerConfig);
        source.registerCorsConfiguration("/**", swaggerConfig);
        return source;
    }
}
