package com.usuario.Ms_usuario;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usuario.Ms_usuario.controller.UsuarioController;
import com.usuario.Ms_usuario.dto.AuthRequest;
import com.usuario.Ms_usuario.dto.RegistroRequest;
import com.usuario.Ms_usuario.exception.UnauthorizedException;
import com.usuario.Ms_usuario.model.Usuario;
import com.usuario.Ms_usuario.service.AccountLockService;
import com.usuario.Ms_usuario.service.UsuarioService;
import com.usuario.Ms_usuario.security.JwtFilter;
import com.usuario.Ms_usuario.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false) // Desactiva los filtros de seguridad para aislar el test del controlador
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private AccountLockService accountLockService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private JwtFilter jwtFilter;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private Usuario usuarioPrueba;
    private RegistroRequest registroRequest;

    @BeforeEach
    void setUp() {
        usuarioPrueba = new Usuario();
        usuarioPrueba.setId(1L);
        usuarioPrueba.setName("Juan Perez");
        usuarioPrueba.setEmail("juan@gmail.com");
        usuarioPrueba.setPassword("secreta123");
        usuarioPrueba.setRol("ciudadano");

        registroRequest = new RegistroRequest("Juan Perez", "juan@gmail.com", "secreta123", "ciudadano", null);
    }

    @Test
    void testRegistrarUsuario() throws Exception {
        Mockito.when(usuarioService.isPasswordStrong(anyString())).thenReturn(true);
        Mockito.when(passwordEncoder.encode(anyString())).thenReturn("passwordEncriptada123");
        Mockito.when(usuarioService.registrar(any(Usuario.class))).thenReturn(usuarioPrueba);

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registroRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Juan Perez"))
                .andExpect(jsonPath("$.data.rol").value("ciudadano"));
    }

    @Test
    void testObtenerTodosLosUsuarios() throws Exception {
        List<Usuario> listaUsuarios = Arrays.asList(usuarioPrueba);
        Mockito.when(usuarioService.obtenerTodos()).thenReturn(listaUsuarios);

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.size()").value(1))
                .andExpect(jsonPath("$.data[0].name").value("Juan Perez"));
    }

    @Test
    void testEliminarUsuario() throws Exception {
        Mockito.doNothing().when(usuarioService).eliminar(1L);

        mockMvc.perform(delete("/api/usuarios/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testLoginExitoso() throws Exception {
        AuthRequest authRequest = new AuthRequest("juan@gmail.com", "secreta123");

        Mockito.when(usuarioService.obtenerPorEmail(anyString())).thenReturn(usuarioPrueba);
        Mockito.when(accountLockService.estaBloqueada(any(Usuario.class))).thenReturn(false);
        Mockito.when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        Mockito.when(jwtUtil.generateToken(anyString())).thenReturn("tokenGenerado123");

        mockMvc.perform(post("/api/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.token").value("tokenGenerado123"))
                .andExpect(jsonPath("$.data.user.name").value("Juan Perez"));
    }

    @Test
    void testLoginCredencialesInvalidas() throws Exception {
        AuthRequest authRequest = new AuthRequest("juan@gmail.com", "claveIncorrecta");

        Mockito.when(usuarioService.obtenerPorEmail(anyString())).thenReturn(usuarioPrueba);
        Mockito.when(accountLockService.estaBloqueada(any(Usuario.class))).thenReturn(false);
        Mockito.when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        mockMvc.perform(post("/api/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testLoginUsuarioNoEncontradoLanzaExcepcion() throws Exception {
        AuthRequest authRequest = new AuthRequest("noexiste@gmail.com", "secreta123");

        Mockito.when(usuarioService.obtenerPorEmail(anyString())).thenThrow(new com.usuario.Ms_usuario.exception.ResourceNotFoundException("Usuario no encontrado"));

        mockMvc.perform(post("/api/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isNotFound());
    }
}