package com.comunidad.Ms_comunidad.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );
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
        return source;
    }
}
