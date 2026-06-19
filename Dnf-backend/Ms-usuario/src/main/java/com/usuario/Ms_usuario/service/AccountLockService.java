package com.usuario.Ms_usuario.service;

import com.usuario.Ms_usuario.model.LoginAttempt;
import com.usuario.Ms_usuario.model.Usuario;
import com.usuario.Ms_usuario.repository.LoginAttemptRepository;
import com.usuario.Ms_usuario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Servicio para gestionar el bloqueo de cuentas por intentos fallidos.
 * Implementa una política de bloqueo temporal (15 minutos) después de N intentos fallidos.
 * 
 * Política actual:
 * - Máximo de intentos fallidos: 5
 * - Ventana de tiempo: 15 minutos
 * - Tiempo de bloqueo: 15 minutos (automático, sin desbloqueo manual)
 */
@Service
public class AccountLockService {

    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final int LOCKOUT_DURATION_MINUTES = 15;
    private static final int TIME_WINDOW_MINUTES = 15;

    @Autowired
    private LoginAttemptRepository loginAttemptRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Registra un intento de login fallido.
     * Si se excede el máximo de intentos en la ventana de tiempo, bloquea la cuenta.
     * 
     * @param usuario Usuario que intentó login
     * @param razonFallo Razón del fallo (ej: "Contraseña incorrecta")
     * @param direccionIp Dirección IP del cliente
     */
    public void registrarIntentoFallido(Usuario usuario, String razonFallo, String direccionIp) {
        // Crear registro del intento fallido
        LocalDateTime ahora = LocalDateTime.now();
        LoginAttempt intento = new LoginAttempt(
            usuario.getId(),
            false,
            ahora,
            direccionIp,
            razonFallo
        );
        loginAttemptRepository.save(intento);

        // Contar intentos fallidos en los últimos N minutos
        LocalDateTime fechaLimite = ahora.minus(TIME_WINDOW_MINUTES, ChronoUnit.MINUTES);
        int intentosFallidos = loginAttemptRepository.contarIntentosFallidos(usuario.getId(), fechaLimite);

        // Si se excede el máximo, bloquear la cuenta
        if (intentosFallidos >= MAX_FAILED_ATTEMPTS) {
            usuario.setActivo(false);
            usuarioRepository.save(usuario);
        }

        // Actualizar contador de intentos en el usuario
        usuario.incrementarIntentosFallidos();
        usuarioRepository.save(usuario);
    }

    /**
     * Registra un intento de login exitoso.
     * Reinicia el contador de intentos fallidos y reactiva la cuenta si estaba bloqueada.
     * 
     * @param usuario Usuario que realizó login exitoso
     * @param direccionIp Dirección IP del cliente
     */
    public void registrarIntentoExitoso(Usuario usuario, String direccionIp) {
        // Crear registro del intento exitoso
        LocalDateTime ahora = LocalDateTime.now();
        LoginAttempt intento = new LoginAttempt(
            usuario.getId(),
            true,
            ahora,
            direccionIp,
            null
        );
        loginAttemptRepository.save(intento);

        // Reiniciar contador de intentos fallidos
        usuario.reiniciarIntentosFallidos();

        // Reactivar cuenta si estaba bloqueada (desbloqueo automático)
        usuario.setActivo(true);

        usuarioRepository.save(usuario);
    }

    /**
     * Verifica si la cuenta del usuario está bloqueada temporalmente.
     * El bloqueo es automático (temporal), basado en la fecha del último intento fallido.
     * 
     * Si han pasado más de LOCKOUT_DURATION_MINUTES desde el último intento fallido,
     * la cuenta se desbloquea automáticamente.
     * 
     * @param usuario Usuario a verificar
     * @return true si la cuenta está bloqueada, false si está disponible
     */
    public boolean estaBloqueada(Usuario usuario) {
        if (!usuario.isActivo()) {
            // Verificar si el bloqueo es temporal o permanente
            LocalDateTime ahora = LocalDateTime.now();
            LocalDateTime fechaDesbloqueo = ahora.minus(LOCKOUT_DURATION_MINUTES, ChronoUnit.MINUTES);

            LoginAttempt ultimoIntento = loginAttemptRepository.obtenerUltimoIntento(usuario.getId());

            if (ultimoIntento != null && ultimoIntento.getFechaIntento().isAfter(fechaDesbloqueo)) {
                // Aún está en período de bloqueo
                return true;
            } else if (ultimoIntento != null) {
                // Ha pasado el período de bloqueo, reactivar automáticamente
                usuario.setActivo(true);
                usuarioRepository.save(usuario);
                return false;
            }
        }
        return false;
    }

    /**
     * Obtiene el tiempo restante de bloqueo (en minutos) de una cuenta.
     * 
     * @param usuario Usuario a verificar
     * @return Minutos restantes de bloqueo (0 si no está bloqueada)
     */
    public long obtenerTiempoRestanteBloqueo(Usuario usuario) {
        if (!usuario.isActivo()) {
            LoginAttempt ultimoIntento = loginAttemptRepository.obtenerUltimoIntento(usuario.getId());
            if (ultimoIntento != null) {
                LocalDateTime fechaDesbloqueo = ultimoIntento.getFechaIntento().plus(LOCKOUT_DURATION_MINUTES, ChronoUnit.MINUTES);
                long minutosRestantes = ChronoUnit.MINUTES.between(LocalDateTime.now(), fechaDesbloqueo);
                return Math.max(0, minutosRestantes);
            }
        }
        return 0;
    }

    /**
     * Obtiene el número máximo de intentos permitidos antes del bloqueo.
     * 
     * @return Número de intentos máximos
     */
    public static int getMaxFailedAttempts() {
        return MAX_FAILED_ATTEMPTS;
    }

    /**
     * Obtiene la duración del bloqueo en minutos.
     * 
     * @return Minutos de bloqueo
     */
    public static int getLockoutDurationMinutes() {
        return LOCKOUT_DURATION_MINUTES;
    }
}
