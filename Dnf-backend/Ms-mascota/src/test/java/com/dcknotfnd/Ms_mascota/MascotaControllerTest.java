package com.dcknotfnd.Ms_mascota;

import com.dcknotfnd.Ms_mascota.controller.MascotaController;
import com.dcknotfnd.Ms_mascota.model.Mascota;
import com.dcknotfnd.Ms_mascota.repository.MascotaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.dcknotfnd.Ms_mascota.security.JwtUtil;
import com.dcknotfnd.Ms_mascota.security.JwtFilter;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MascotaController.class)
@AutoConfigureMockMvc(addFilters = false)
public class MascotaControllerTest {

    @Autowired
    private MockMvc mockMvc; // Simula las peticiones de Postman

    @MockBean
    private MascotaRepository mascotaRepository; // Simula la base de datos

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private JwtFilter jwtFilter;

    @Autowired
    private ObjectMapper objectMapper; // Convierte objetos Java a JSON

    // Aquí guardaremos nuestros "datos de prueba"
    private Mascota mascotaPrueba;

    @BeforeEach
    void setUp() {
        // Configuramos los DATOS usando los nuevos atributos en inglés
        mascotaPrueba = new Mascota();
        mascotaPrueba.setId(1L);
        mascotaPrueba.setName("Firulais"); // Compilación: ajustado a name
        mascotaPrueba.setType("Perro");    // Compilación: ajustado a type
        mascotaPrueba.setBreed("Quiltro"); // Compilación: ajustado a breed
        mascotaPrueba.setColor("Negro");
        // Compilación: se eliminó setTamano ya que no existe en el modelo actual
        mascotaPrueba.setUsuarioId(10L);
        mascotaPrueba.setStatus("lost");
    }

    @Test
    void testRegistrarMascota() throws Exception {
        // 1. Preparamos el comportamiento
        Mockito.when(mascotaRepository.save(any(Mascota.class))).thenReturn(mascotaPrueba);

        // 2. Hacemos el POST simulado a la nueva ruta /api/pets/report
        mockMvc.perform(post("/api/pets/report")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mascotaPrueba)))

                // 3. Verificamos que responda 201 Created y que el JSON de respuesta sea el correcto
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Reporte creado exitosamente"));
    }

    @Test
    void testObtenerTodasLasMascotas() throws Exception {
        // 1. Preparamos una lista de datos
        List<Mascota> listaDatos = Arrays.asList(mascotaPrueba);

        // Prueba: ajustado para mockear el nuevo método buscarConFiltros en lugar de findAll
        Mockito.when(mascotaRepository.buscarConFiltros(any(), any(), any())).thenReturn(listaDatos);

        // 2. Hacemos el GET simulado a /api/pets
        mockMvc.perform(get("/api/pets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1)) // Verificamos que traiga 1 mascota
                .andExpect(jsonPath("$[0].name").value("Firulais")); // Prueba: Verificamos el name
    }

    @Test
    void testObtenerMascotaPorId() throws Exception {
        // 1. Preparamos los datos
        Mockito.when(mascotaRepository.findById(1L)).thenReturn(Optional.of(mascotaPrueba));

        // 2. Hacemos el GET simulado al endpoint específico de ID
        mockMvc.perform(get("/api/pets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Firulais"))
                .andExpect(jsonPath("$.status").value("lost"));
    }

    @Test
    void testObtenerTodasLasMascotasConFiltros() throws Exception {
        List<Mascota> listaDatos = Arrays.asList(mascotaPrueba);
        Mockito.when(mascotaRepository.buscarConFiltros("lost", "Perro", "Firulais")).thenReturn(listaDatos);

        mockMvc.perform(get("/api/pets?status=lost&type=Perro&query=Firulais"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void testObtenerSugerencias() throws Exception {
        List<Mascota> listaDatos = Arrays.asList(mascotaPrueba);
        Mockito.when(mascotaRepository.findByNameContainingIgnoreCase("Firu")).thenReturn(listaDatos);

        mockMvc.perform(get("/api/pets/suggestions?q=Firu"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void testObtenerPorUsuario() throws Exception {
        List<Mascota> listaDatos = Arrays.asList(mascotaPrueba);
        Mockito.when(mascotaRepository.findByUsuarioId(10L)).thenReturn(listaDatos);

        mockMvc.perform(get("/api/pets/usuario/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void testObtenerMascotaPorId_NotFound() throws Exception {
        Mockito.when(mascotaRepository.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/pets/99"))
                .andExpect(status().isNotFound());
    }
}