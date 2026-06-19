package com.dcknotfnd.ms_coincidencias;

import com.dcknotfnd.ms_coincidencias.client.MascotaClient;
import com.dcknotfnd.ms_coincidencias.controller.CoincidenciaController;
import com.dcknotfnd.ms_coincidencias.dto.MascotaDTO;
import com.dcknotfnd.ms_coincidencias.model.Coincidencia;
import com.dcknotfnd.ms_coincidencias.repository.CoincidenciaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.dcknotfnd.ms_coincidencias.security.JwtUtil;
import com.dcknotfnd.ms_coincidencias.security.JwtFilter;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CoincidenciaController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CoincidenciaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CoincidenciaRepository coincidenciaRepository;

    @MockBean
    private MascotaClient mascotaClient;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private JwtFilter jwtFilter;

    @Autowired
    private ObjectMapper objectMapper;

    private Coincidencia coincidenciaPrueba;
    private MascotaDTO mascotaDTOPrueba;

    @BeforeEach
    void setUp() {
        coincidenciaPrueba = new Coincidencia(1L, "Perro", "Blanco", "PENDIENTE");
        
        // DTO Base (Coincidencia Perfecta)
        mascotaDTOPrueba = new MascotaDTO();
        mascotaDTOPrueba.setId(10L);
        mascotaDTOPrueba.setType("Perro");
        mascotaDTOPrueba.setBreed("Blanco"); 
    }

    @Test
    void testCrearCoincidencia() throws Exception {
        Mockito.when(coincidenciaRepository.save(any(Coincidencia.class))).thenReturn(coincidenciaPrueba);

        mockMvc.perform(post("/api/coincidencias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(coincidenciaPrueba)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.especieBuscada").value("Perro"))
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));
    }

    @Test
    void testListarCoincidencias() throws Exception {
        List<Coincidencia> lista = Arrays.asList(coincidenciaPrueba);
        Mockito.when(coincidenciaRepository.findAll()).thenReturn(lista);

        mockMvc.perform(get("/api/coincidencias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].colorBuscado").value("Blanco"));
    }

    // --- TESTS DE COBERTURA: RAMAS DEL FILTER ---

    @Test
    void testBuscarCoincidenciasExito() throws Exception {
        // Coincide especie y color
        List<MascotaDTO> listaMascotas = Arrays.asList(mascotaDTOPrueba);
        Mockito.when(mascotaClient.obtenerTodasLasMascotas()).thenReturn(listaMascotas);

        mockMvc.perform(get("/api/coincidencias/buscar")
                        .param("especie", "Perro")
                        .param("color", "Blanco"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].type").value("Perro"));
    }

    @Test
    void testBuscarCoincidenciasFallaColor() throws Exception {
        // Coincide especie pero falla color
        MascotaDTO mascotaFallaColor = new MascotaDTO();
        mascotaFallaColor.setId(11L);
        mascotaFallaColor.setType("Perro");
        mascotaFallaColor.setBreed("Negro"); 

        List<MascotaDTO> listaMascotas = Arrays.asList(mascotaDTOPrueba, mascotaFallaColor);
        Mockito.when(mascotaClient.obtenerTodasLasMascotas()).thenReturn(listaMascotas);

        mockMvc.perform(get("/api/coincidencias/buscar")
                        .param("especie", "Perro")
                        .param("color", "Blanco"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void testBuscarCoincidenciasFallaEspecie() throws Exception {
        // Falla especie pero coincide color
        MascotaDTO mascotaFallaEspecie = new MascotaDTO();
        mascotaFallaEspecie.setId(12L);
        mascotaFallaEspecie.setType("Gato");
        mascotaFallaEspecie.setBreed("Blanco");

        List<MascotaDTO> listaMascotas = Arrays.asList(mascotaDTOPrueba, mascotaFallaEspecie);
        Mockito.when(mascotaClient.obtenerTodasLasMascotas()).thenReturn(listaMascotas);

        mockMvc.perform(get("/api/coincidencias/buscar")
                        .param("especie", "Perro")
                        .param("color", "Blanco"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void testBuscarCoincidenciasFallaAmbas() throws Exception {
        // No coincide nada
        MascotaDTO mascotaFallaAmbas = new MascotaDTO();
        mascotaFallaAmbas.setId(13L);
        mascotaFallaAmbas.setType("Pájaro");
        mascotaFallaAmbas.setBreed("Azul");

        List<MascotaDTO> listaMascotas = Collections.singletonList(mascotaFallaAmbas);
        Mockito.when(mascotaClient.obtenerTodasLasMascotas()).thenReturn(listaMascotas);

        mockMvc.perform(get("/api/coincidencias/buscar")
                        .param("especie", "Perro")
                        .param("color", "Blanco"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }

    // --- TESTS DE COBERTURA: ACTUALIZAR Y ELIMINAR ---

    @Test
    void testActualizarCoincidenciaExito() throws Exception {
        Mockito.when(coincidenciaRepository.findById(1L)).thenReturn(Optional.of(coincidenciaPrueba));
        
        Coincidencia coincidenciaActualizada = new Coincidencia(1L, "Perro", "Blanco", "RESUELTO");
        Mockito.when(coincidenciaRepository.save(any(Coincidencia.class))).thenReturn(coincidenciaActualizada);

        mockMvc.perform(put("/api/coincidencias/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(coincidenciaActualizada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("RESUELTO"));
    }

    @Test
    void testActualizarCoincidenciaNoEncontrada() throws Exception {
        Mockito.when(coincidenciaRepository.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/coincidencias/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(coincidenciaPrueba)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof com.dcknotfnd.ms_coincidencias.exception.ResourceNotFoundException));
    }

    @Test
    void testEliminarCoincidencia() throws Exception {
        mockMvc.perform(delete("/api/coincidencias/1"))
                .andExpect(status().isOk());

        Mockito.verify(coincidenciaRepository, Mockito.times(1)).deleteById(1L);
    }
}