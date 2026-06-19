package com.usuario.Ms_usuario.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Interceptor para auditoría de peticiones HTTP.
 * Registra información sobre cada petición y respuesta.
 */
@Component
public class AuditInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(AuditInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);

        String method = request.getMethod();
        String path = request.getRequestURI();
        String remoteAddr = getClientIP(request);
        String user = request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "ANONYMOUS";

        logger.info("[AUDIT] {} {} - User: {} - IP: {}", method, path, user, remoteAddr);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        long startTime = (long) request.getAttribute("startTime");
        long duration = System.currentTimeMillis() - startTime;

        String method = request.getMethod();
        String path = request.getRequestURI();
        int status = response.getStatus();
        String user = request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "ANONYMOUS";

        if (ex != null) {
            logger.error("[AUDIT] {} {} returned {} - User: {} - Duration: {}ms - Error: {}", 
                method, path, status, user, duration, ex.getMessage());
        } else {
            logger.info("[AUDIT] {} {} returned {} - User: {} - Duration: {}ms", 
                method, path, status, user, duration);
        }
    }

    /**
     * Obtiene la dirección IP del cliente.
     * Considera proxies y forwarded headers.
     */
    private String getClientIP(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0];
        }
        
        String xRealIP = request.getHeader("X-Real-IP");
        if (xRealIP != null && !xRealIP.isEmpty()) {
            return xRealIP;
        }
        
        return request.getRemoteAddr();
    }
}
