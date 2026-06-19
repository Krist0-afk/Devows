package com.usuario.Ms_usuario.config;

import com.usuario.Ms_usuario.interceptor.AuditInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración de interceptores de la aplicación.
 * Registra el AuditInterceptor para auditar todas las peticiones.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AuditInterceptor auditInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(auditInterceptor)
            .addPathPatterns("/**")
            .excludePathPatterns("/swagger-ui/**", "/v3/api-docs/**", "/actuator/**");
    }
}
