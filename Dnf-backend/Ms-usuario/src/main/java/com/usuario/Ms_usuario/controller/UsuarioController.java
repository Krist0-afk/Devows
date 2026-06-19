package com.usuario.Ms_usuario.controller;

import com.usuario.Ms_usuario.dto.*;
import com.usuario.Ms_usuario.exception.AccountLockedException;
import com.usuario.Ms_usuario.exception.UnauthorizedException;
import com.usuario.Ms_usuario.model.Usuario;
import com.usuario.Ms_usuario.service.AccountLockService;
import com.usuario.Ms_usuario.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.usuario.Ms_usuario.security.JwtUtil;

import java.util.List;

/**
 * Controlador para gestionar usuarios y autenticación.
 * Proporciona endpoints para registro, login y gestión de usuarios.
 * 
 * Endpoints públicos:
 * - POST /api/usuarios (registro)
 * - POST /api/usuarios/login (login)
 * 
 * Endpoints protegidos:
 * - GET /api/usuarios (requiere cualquier autenticación)
 * - GET /api/usuarios/{id} (requiere cualquier autenticación)
 * - DELETE /api/usuarios/{id} (requiere rol ADMIN)
 */
@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "API de Usuarios", description = "Controlador para gestionar el registro y autenticación de usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AccountLockService accountLockService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Registra un nuevo usuario con validaciones de seguridad.
     * ENDPOINT PÚBLICO
     * 
     * @param request Datos de registro con validaciones
     * @return Respuesta con datos del usuario registrado
     */
    @Operation(summary = "Registrar un nuevo usuario", description = "Registra un nuevo usuario con validaciones de email y contraseña fuerte")
    @PostMapping
    public ResponseEntity<ApiResponse<UsuarioDTO>> registrar(@Valid @RequestBody RegistroRequest request) {
        // Validar que la contraseña sea fuerte
        if (!usuarioService.isPasswordStrong(request.password())) {
            throw new IllegalArgumentException(
                "La contraseña debe contener mayúsculas, minúsculas, números y caracteres especiales"
            );
        }

        // Crear usuario
        Usuario usuario = new Usuario();
        usuario.setName(request.name());
        usuario.setEmail(request.email());
        usuario.setPassword(passwordEncoder.encode(request.password()));
        usuario.setRol(request.rol());
        usuario.setAvatar(request.avatar());

        // Registrar (validará email único)
        Usuario nuevoUsuario = usuarioService.registrar(usuario);
        UsuarioDTO usuarioDTO = convertirADTO(nuevoUsuario);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success(201, "Usuario registrado exitosamente", usuarioDTO));
    }

    /**
     * Realiza login del usuario y genera token JWT.
     * ENDPOINT PÚBLICO
     * 
     * Incluye mecanismo de protección contra ataques de fuerza bruta:
     * - Máximo 5 intentos fallidos en 15 minutos
     * - Bloqueo automático de cuenta por 15 minutos
     * - Desbloqueo automático después del período de bloqueo
     * 
     * @param request Email y contraseña
     * @param httpRequest Request HTTP para obtener IP del cliente
     * @return Token JWT y datos del usuario
     * @throws AccountLockedException si la cuenta está bloqueada
     * @throws UnauthorizedException si las credenciales son inválidas
     */
    @Operation(summary = "Login de usuario", description = "Autentica un usuario y retorna un token JWT con protección contra fuerza bruta")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody AuthRequest request,
            HttpServletRequest httpRequest) {
        
        // Obtener IP del cliente
        String clientIp = getClientIp(httpRequest);
        
        Usuario usuario = usuarioService.obtenerPorEmail(request.email());

        // Verificar si la cuenta está bloqueada
        if (accountLockService.estaBloqueada(usuario)) {
            long minutosRestantes = accountLockService.obtenerTiempoRestanteBloqueo(usuario);
            throw new AccountLockedException(
                "Cuenta bloqueada por múltiples intentos fallidos",
                minutosRestantes
            );
        }

        // Validar contraseña
        if (!passwordEncoder.matches(request.password(), usuario.getPassword())) {
            // Registrar intento fallido
            accountLockService.registrarIntentoFallido(
                usuario,
                "Contraseña incorrecta",
                clientIp
            );
            throw new UnauthorizedException("Credenciales inválidas");
        }

        // Registrar intento exitoso (reinicia contador)
        accountLockService.registrarIntentoExitoso(usuario, clientIp);

        // Generar token
        String token = jwtUtil.generateToken(usuario.getEmail());
        UsuarioDTO usuarioDTO = convertirADTO(usuario);
        AuthResponse authResponse = new AuthResponse(token, usuarioDTO);

        return ResponseEntity.ok(ApiResponse.success("Login exitoso", authResponse));
    }

    /**
     * Obtiene la lista de todos los usuarios.
     * ENDPOINT PROTEGIDO: Requiere estar autenticado
     * 
     * @return Lista de usuarios sin contraseñas
     */
    @Operation(summary = "Listar todos los usuarios", description = "Obtiene una lista de todos los usuarios registrados")
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<UsuarioDTO>>> obtenerTodos() {
        List<Usuario> usuarios = usuarioService.obtenerTodos();
        List<UsuarioDTO> usuariosDTO = usuarios.stream()
            .map(this::convertirADTO)
            .toList();
        
        return ResponseEntity.ok(ApiResponse.success("Usuarios obtenidos exitosamente", usuariosDTO));
    }

    /**
     * Obtiene un usuario por ID.
     * ENDPOINT PROTEGIDO: Requiere estar autenticado
     * 
     * @param id ID del usuario
     * @return Datos del usuario sin contraseña
     */
    @Operation(summary = "Obtener usuario por ID", description = "Obtiene los datos de un usuario específico")
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<UsuarioDTO>> obtenerPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.obtenerPorId(id);
        UsuarioDTO usuarioDTO = convertirADTO(usuario);
        return ResponseEntity.ok(ApiResponse.success("Usuario obtenido exitosamente", usuarioDTO));
    }

    /**
     * Elimina un usuario por ID.
     * ENDPOINT PROTEGIDO: Requiere rol ADMIN
     * 
     * @param id ID del usuario a eliminar
     * @return Mensaje de confirmación
     */
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario de la base de datos (solo ADMIN)")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.success("Usuario eliminado exitosamente", null));
    }

    /**
     * Obtiene el historial de intentos de login de un usuario.
     * ENDPOINT PROTEGIDO: Requiere estar autenticado
     * Solo un usuario puede ver su propio historial, o un ADMIN puede ver cualquiera.
     * 
     * @param usuarioId ID del usuario
     * @param limit Número máximo de registros (default: 50)
     * @return Historial de login con detalles de intentos exitosos/fallidos
     */
    @Operation(summary = "Obtener historial de login", description = "Obtiene el historial de intentos de login de un usuario")
    @GetMapping("/{usuarioId}/login-audit")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<LoginAuditDTO>>> obtenerHistorialLogin(
            @PathVariable Long usuarioId,
            @RequestParam(defaultValue = "50") int limit) {
        
        // Validar que el usuario exista
        usuarioService.obtenerPorId(usuarioId);
        
        // Obtener historial
        List<LoginAuditDTO> historial = usuarioService.obtenerHistorialLogin(usuarioId, limit);
        
        return ResponseEntity.ok(ApiResponse.success("Historial de login obtenido exitosamente", historial));
    }

    /**
     * Convierte una entidad Usuario a DTO seguro (sin contraseña).
     * 
     * @param usuario Usuario a convertir
     * @return UsuarioDTO sin información sensible
     */
    private UsuarioDTO convertirADTO(Usuario usuario) {
        return new UsuarioDTO(
            usuario.getId(),
            usuario.getName(),
            usuario.getEmail(),
            usuario.getRol(),
            usuario.getAvatar()
        );
    }

    /**
     * Obtiene la dirección IP del cliente.
     * Considera proxies y load balancers (X-Forwarded-For header).
     * 
     * @param request HttpServletRequest
     * @return IP del cliente
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        
        // Si hay múltiples IPs en X-Forwarded-For, obtener la primera
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        
        return ip;
    }
}



