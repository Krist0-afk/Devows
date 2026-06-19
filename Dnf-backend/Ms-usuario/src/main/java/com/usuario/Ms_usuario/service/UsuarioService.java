package com.usuario.Ms_usuario.service;

import com.usuario.Ms_usuario.dto.LoginAuditDTO;
import com.usuario.Ms_usuario.exception.ConflictException;
import com.usuario.Ms_usuario.exception.ResourceNotFoundException;
import com.usuario.Ms_usuario.model.LoginAttempt;
import com.usuario.Ms_usuario.model.Usuario;
import com.usuario.Ms_usuario.repository.LoginAttemptRepository;
import com.usuario.Ms_usuario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio para operaciones relacionadas con usuarios.
 */
@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private LoginAttemptRepository loginAttemptRepository;

    /**
     * Verifica si un email ya existe en la base de datos.
     * 
     * @param email Email a verificar
     * @return true si el email existe, false en caso contrario
     */
    public boolean emailExists(String email) {
        return usuarioRepository.findByEmail(email).isPresent();
    }

    /**
     * Obtiene un usuario por email.
     * Lanza excepción si no lo encuentra.
     * 
     * @param email Email del usuario
     * @return Usuario encontrado
     * @throws ResourceNotFoundException si no existe
     */
    @Cacheable(value = "usuarios", key = "#email")
    public Usuario obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario con email " + email + " no encontrado"));
    }

    /**
     * Valida la fortaleza de una contraseña.
     * Requiere:
     * - Al menos 8 caracteres
     * - Al menos una mayúscula
     * - Al menos una minúscula
     * - Al menos un número
     * - Al menos un carácter especial
     * 
     * @param password Contraseña a validar
     * @return true si es fuerte, false en caso contrario
     */
    public boolean isPasswordStrong(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasUpperCase = password.matches(".*[A-Z].*");
        boolean hasLowerCase = password.matches(".*[a-z].*");
        boolean hasDigit = password.matches(".*\\d.*");
        boolean hasSpecial = password.matches(".*[@$!%*?&].*");

        return hasUpperCase && hasLowerCase && hasDigit && hasSpecial;
    }

    /**
     * Obtiene un usuario por ID.
     * Lanza excepción si no lo encuentra.
     * 
     * @param id ID del usuario
     * @return Usuario encontrado
     * @throws ResourceNotFoundException si no existe
     */
    @Cacheable(value = "usuarios", key = "#id")
    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario con ID " + id + " no encontrado"));
    }

    /**
     * Registra un nuevo usuario.
     * Valida que el email no exista previamente.
     * 
     * @param usuario Usuario a registrar
     * @return Usuario registrado
     * @throws ConflictException si el email ya existe
     */
    public Usuario registrar(Usuario usuario) {
        if (emailExists(usuario.getEmail())) {
            throw new ConflictException("El email " + usuario.getEmail() + " ya está registrado");
        }
        return usuarioRepository.save(usuario);
    }

    /**
     * Obtiene todos los usuarios.
     * 
     * @return Lista de usuarios
     */
    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    /**
     * Elimina un usuario por ID.
     * 
     * @param id ID del usuario a eliminar
     * @throws ResourceNotFoundException si el usuario no existe
     */
    public void eliminar(Long id) {
        Usuario usuario = obtenerPorId(id);
        usuarioRepository.deleteById(id);
    }

    /**
     * Obtiene el historial de intentos de login de un usuario.
     * 
     * @param usuarioId ID del usuario
     * @param limit Número máximo de registros a retornar
     * @return Lista de DTOs con información de auditoría de login
     */
    public List<LoginAuditDTO> obtenerHistorialLogin(Long usuarioId, int limit) {
        List<LoginAttempt> intentos = loginAttemptRepository.obtenerHistorialLogins(usuarioId, limit);
        return intentos.stream()
            .map(intento -> new LoginAuditDTO(
                intento.getId(),
                intento.getUsuarioId(),
                intento.isIntentoExitoso(),
                intento.getFechaIntento(),
                intento.getDireccionIp(),
                intento.getRazonFallo()
            ))
            .toList();
    }
}


