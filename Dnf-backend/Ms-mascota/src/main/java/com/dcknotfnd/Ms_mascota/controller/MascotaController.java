package com.dcknotfnd.Ms_mascota.controller;

import com.dcknotfnd.Ms_mascota.model.Mascota;
import com.dcknotfnd.Ms_mascota.repository.MascotaRepository;
import com.dcknotfnd.Ms_mascota.dto.MascotaPerdidaDTO;
import com.dcknotfnd.Ms_mascota.config.RabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import com.dcknotfnd.Ms_mascota.service.MascotaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pets")
@Tag(name = "API de Mascotas", description = "Controlador para gestionar reportes y búsquedas de mascotas")
public class MascotaController {

    @Autowired
    private MascotaRepository mascotaRepository;

    @Autowired
    private MascotaService mascotaService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Operation(summary = "Listar Mascotas con Filtros", description = "Obtiene la lista de mascotas aplicando filtros opcionales del mapa.")
    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    @GetMapping
    public ResponseEntity<List<Mascota>> obtenerMascotas(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String query) {

        List<Mascota> mascotas = mascotaService.obtenerMascotas(status, type, query);
        return ResponseEntity.ok(mascotas);
    }

    /*
    MascotaCOntroller se encarga de gestionar los reportes de mascotas,
    aqui se pueden listar mascotas con filtros, reportar una mascota perdida o encontrada

    @param status: Estado de la mascota (perdida, encontrada, etc.)
    @param type: Tipo de mascota (perro, gato, etc.)
    @return: Lista de mascotas que coinciden con los filtros aplicados
     */
    @Operation(summary = "Reportar una mascota", description = "Crea un nuevo reporte (perdida o encontrada).")
    @ApiResponse(responseCode = "201", description = "Reporte creado exitosamente")
    @PostMapping("/report")
    public ResponseEntity<Map<String, Object>> reportarMascota(@RequestBody Mascota mascota) {

   
        Mascota nuevaMascota = mascotaRepository.save(mascota);

        if ("perdida".equalsIgnoreCase(nuevaMascota.getStatus()) || "lost".equalsIgnoreCase(nuevaMascota.getStatus())) {
            MascotaPerdidaDTO dto = new MascotaPerdidaDTO(
                nuevaMascota.getName(),
                nuevaMascota.getType(),
                nuevaMascota.getDescription(),
                nuevaMascota.getLocation(),
                "Usuario " + nuevaMascota.getUsuarioId(),
                "usuario" + nuevaMascota.getUsuarioId() + "@dnf.com",
                "No disponible"
            );
            rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_NAME, dto);
        }

        
        Map<String, Object> response = new HashMap<>();
        response.put("id", nuevaMascota.getId());
        response.put("status", "success");
        response.put("message", "Reporte creado exitosamente");

       
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Sugerencias de búsqueda", description = "Autocompletado en tiempo real por nombre.")
    @GetMapping("/suggestions")
    public ResponseEntity<List<Mascota>> obtenerSugerencias(@RequestParam("q") String q) {
        List<Mascota> sugerencias = mascotaRepository.findByNameContainingIgnoreCase(q);
        return ResponseEntity.ok(sugerencias);
    }
    @Operation(summary = "Obtener mascotas de un usuario", description = "Lista el historial de reportes de un usuario específico.")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Mascota>> obtenerPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(mascotaRepository.findByUsuarioId(usuarioId));
    }
    

    @GetMapping("/{id}")
    public ResponseEntity<Mascota> obtenerMascotaPorId(@PathVariable Long id) {
        return mascotaService.obtenerMascotaPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


}
/*
aqui se define el controlador de mascotas, que se encarga de gestionar los reportes de mascotas, 
como listar mascotas con filtros, reportar una mascota perdida o encontrada,
 obtener sugerencias de búsqueda por nombre y obtener el historial de reportes de un usuario específico. 

 @param status: Estado de la mascota (perdida, encontrada, etc.)
 @param type: Tipo de mascota (perro, gato, etc.)
@return entrega una lista de mascotas que coinciden con los filtros aplicados, o el historial de reportes de un usuario específico, o el detalle de una mascota por su id.
*/