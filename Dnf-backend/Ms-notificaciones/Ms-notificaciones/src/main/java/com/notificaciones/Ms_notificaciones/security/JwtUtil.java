package com.notificaciones.Ms_notificaciones.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Utilidad para validación de tokens JWT en Ms-notificaciones.
 * Se encarga de validar la integridad y expiración de los tokens.
 */
@Component
public class JwtUtil {

    private final SecretKey key;

    public JwtUtil(@Value("${jwt.secret:EstaEsUnaClaveSecretaMuyLargaParaAsegurarElTokenDeSanosYSalvos2026}") String secretString) {
        this.key = Keys.hmacShaKeyFor(secretString.getBytes());
    }

    /**
     * Extrae el email (subject) del token.
     * 
     * @param token Token JWT
     * @return Email del usuario
     */
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * Valida si el token es correcto, no ha expirado y tiene la firma válida.
     * 
     * @param token Token JWT
     * @return true si el token es válido, false en caso contrario
     */
    public boolean isTokenValid(String token) {
        try {
            Claims claims = getClaims(token);
            
            // Validar que el token no esté expirado
            if (claims.getExpiration() == null || claims.getExpiration().before(new Date())) {
                return false;
            }
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica si el token está expirado.
     * 
     * @param token Token JWT
     * @return true si está expirado, false en caso contrario
     */
    public boolean isTokenExpired(String token) {
        Date expiration = getExpiration(token);
        return expiration != null && expiration.before(new Date());
    }

    /**
     * Extrae la fecha de expiración del token.
     * 
     * @param token Token JWT
     * @return Fecha de expiración
     */
    public Date getExpiration(String token) {
        return getClaims(token).getExpiration();
    }

    /**
     * Extrae los claims del token.
     * 
     * @param token Token JWT
     * @return Claims del token
     */
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
