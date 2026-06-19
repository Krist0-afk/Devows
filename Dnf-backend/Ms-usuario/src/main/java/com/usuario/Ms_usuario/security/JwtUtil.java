package com.usuario.Ms_usuario.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Utilidad para generación y validación de tokens JWT.
 * Se encarga de crear tokens JWT con expiración y validar su integridad.
 */
@Component
public class JwtUtil {

    private final SecretKey key;
    private static final long TOKEN_EXPIRATION_TIME = 1000 * 60 * 60 * 10; // 10 horas

    public JwtUtil(@Value("${jwt.secret:EstaEsUnaClaveSecretaMuyLargaParaAsegurarElTokenDeSanosYSalvos2026}") String secretString) {
        this.key = Keys.hmacShaKeyFor(secretString.getBytes());
    }

    /**
     * Genera un nuevo token JWT para el usuario.
     * El token expira después de 10 horas.
     * 
     * @param email Email del usuario
     * @return Token JWT generado
     */
    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME))
                .signWith(key)
                .compact();
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
            // Si la firma es inválida, está mal formado o expiró, cae aquí
            return false;
        }
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
