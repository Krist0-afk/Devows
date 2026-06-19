package com.usuario.Ms_usuario;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usuario.Ms_usuario.dto.AuthRequest;
import com.usuario.Ms_usuario.model.Usuario;
import com.usuario.Ms_usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "eureka.client.enabled=false"
})
@AutoConfigureMockMvc
public class LoginIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        usuarioRepository.deleteAll();
        Usuario u = new Usuario();
        u.setName("Test User");
        u.setEmail("test@gmail.com");
        u.setPassword(passwordEncoder.encode("supersecret"));
        u.setRol("ciudadano");
        usuarioRepository.save(u);
    }

    @Test
    void testRealLoginGeneratesJwtToken() throws Exception {
        AuthRequest request = new AuthRequest("test@gmail.com", "supersecret");

        MvcResult result = mockMvc.perform(post("/api/usuarios/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        
        // Verificamos que la respuesta contenga un token y el formato de JWT (tres partes separadas por punto)
        assertTrue(responseBody.contains("\"token\""));
        
        // Extraemos el token para verificar visualmente su formato
        String token = responseBody.split("\"token\":\"")[1].split("\"")[0];
        assertNotNull(token);
        assertTrue(token.split("\\.").length == 3, "El JWT generado debe tener 3 partes (Header.Payload.Signature)");
        
        System.out.println("=======================================================================");
        System.out.println("VERIFICACIÓN EXITOSA: EL LOGIN GENERÓ UN TOKEN JWT REAL Y FUNCIONAL");
        System.out.println("TOKEN GENERADO: " + token);
        System.out.println("=======================================================================");
    }
}
