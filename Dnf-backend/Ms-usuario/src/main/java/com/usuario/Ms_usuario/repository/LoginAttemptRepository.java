package com.usuario.Ms_usuario.repository;

import com.usuario.Ms_usuario.model.LoginAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio para gestionar registros de intentos de login.
 * Proporciona métodos para consultar y guardar intentos de login.
 */
@Repository
public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, Long> {

    /**
     * Obtiene los intentos fallidos más recientes de un usuario dentro de un período de tiempo.
     * 
     * @param usuarioId ID del usuario
     * @param fechaDesde Fecha desde la cual contar intentos fallidos
     * @return Lista de intentos fallidos recientes
     */
    @Query(value = "SELECT * FROM login_attempts WHERE usuario_id = :usuarioId AND intento_exitoso = false AND fecha_intento > :fechaDesde ORDER BY fecha_intento DESC", nativeQuery = true)
    List<LoginAttempt> obtenerIntentosFallidosRecientes(@Param("usuarioId") Long usuarioId, @Param("fechaDesde") LocalDateTime fechaDesde);

    /**
     * Cuenta los intentos fallidos de un usuario en los últimos N minutos.
     * 
     * @param usuarioId ID del usuario
     * @param fechaDesde Fecha desde la cual contar
     * @return Número de intentos fallidos
     */
    @Query(value = "SELECT COUNT(*) FROM login_attempts WHERE usuario_id = :usuarioId AND intento_exitoso = false AND fecha_intento > :fechaDesde", nativeQuery = true)
    int contarIntentosFallidos(@Param("usuarioId") Long usuarioId, @Param("fechaDesde") LocalDateTime fechaDesde);

    /**
     * Obtiene el último intento de login (exitoso o fallido) de un usuario.
     * 
     * @param usuarioId ID del usuario
     * @return El último LoginAttempt del usuario
     */
    @Query(value = "SELECT * FROM login_attempts WHERE usuario_id = :usuarioId ORDER BY fecha_intento DESC LIMIT 1", nativeQuery = true)
    LoginAttempt obtenerUltimoIntento(@Param("usuarioId") Long usuarioId);

    /**
     * Obtiene el historial de intentos de login de un usuario.
     * 
     * @param usuarioId ID del usuario
     * @param limit Número máximo de registros a retornar
     * @return Lista de intentos ordenados por fecha descendente
     */
    @Query(value = "SELECT * FROM login_attempts WHERE usuario_id = :usuarioId ORDER BY fecha_intento DESC LIMIT :limit", nativeQuery = true)
    List<LoginAttempt> obtenerHistorialLogins(@Param("usuarioId") Long usuarioId, @Param("limit") int limit);
}
