package com.comunidad.Ms_comunidad.controller;

import com.comunidad.Ms_comunidad.model.Historia;
import com.comunidad.Ms_comunidad.repository.HistoriaRepository;
import com.comunidad.Ms_comunidad.security.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HistoriaController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = {
        "eureka.client.enabled=false",
        "spring.cloud.discovery.enabled=false"
})
public class HistoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HistoriaRepository historiaRepository;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private Historia historiaPrueba;

    @BeforeEach
    void setUp() {
        historiaPrueba = new Historia();
        historiaPrueba.setId(1L);
        historiaPrueba.setTitle("Una mascota feliz");
        historiaPrueba.setFamily("Familia Baez");
        historiaPrueba.setLocation("Puerto Montt");
        historiaPrueba.setContent("Se adoptó y ahora es muy feliz.");
        historiaPrueba.setImage("url_imagen");
        historiaPrueba.setTimeAgo("1 mes");
    }

    @Test
    void testObtenerHistorias() throws Exception {
        Mockito.when(historiaRepository.findAll()).thenReturn(Arrays.asList(historiaPrueba));

        mockMvc.perform(get("/api/success-stories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].title").value("Una mascota feliz"))
                .andExpect(jsonPath("$[0].location").value("Puerto Montt"));
    }

    @Test
    void testCrearHistoria() throws Exception {
        Mockito.when(historiaRepository.save(any())).thenReturn(historiaPrueba);

        mockMvc.perform(post("/api/success-stories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(historiaPrueba)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.family").value("Familia Baez"));
    }
}