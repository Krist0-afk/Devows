package com.dcknotfnd.ms_coincidencias.controller;

import com.dcknotfnd.ms_coincidencias.client.MascotaClient;
import com.dcknotfnd.ms_coincidencias.dto.MascotaDTO;
import com.dcknotfnd.ms_coincidencias.model.Coincidencia;
import com.dcknotfnd.ms_coincidencias.repository.CoincidenciaRepository;
import com.dcknotfnd.ms_coincidencias.service.CoincidenciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coincidencias")
@Tag(name = "API Coincidencias", description = "Gestión de coincidencias de mascotas")
public class CoincidenciaController {


    @Autowired
    private CoincidenciaService coincidenciaService;

    @Autowired
    private CoincidenciaRepository coincidenciaRepository;

    @GetMapping("/buscar")
    public List<MascotaDTO> buscarCoincidencias(@RequestParam String especie, @RequestParam String color) {
        return coincidenciaService.buscarCoincidencias(especie, color);
    }
     /*
    Aqui se hace la busqueda de mascota base las especificaciones que se le den
    como color, raza, especie, etc.

    @param especie: Especie de la mascota a buscar (perro, gato, etc.)
    @param color: Color de la mascota 
    @return: Lista de mascotas que coinciden con los criterios de búsqueda
    
    */
    

    @Operation(summary = "Crear nueva coincidencia", description = "Registra una búsqueda en estado PENDIENTE.")
    @PostMapping
    public Coincidencia crearCoincidencia(@Valid @RequestBody Coincidencia coincidencia) {
        coincidencia.setEstado("PENDIENTE");
        return coincidenciaRepository.save(coincidencia);
    }
    /*
    aqui se crea una coincidencia, o regitra el estado de la busqueda
    @param coincidencia: Objeto que contiene la información de la búsqueda a registrar
    @return: La coincidencia registrada con su estado inicial (PENDIENTE)
    */

    @Operation(summary = "Listar coincidencias", description = "Muestra todas las coincidencias registradas.")
    @GetMapping
    public List<Coincidencia> listarCoincidencias() {
        return coincidenciaRepository.findAll();
    }
    /*
    Aqui se listan las coincidencias Registradas en la base de datos
    @return: Lista de todas las coincidencias registradas
    */

    @PutMapping("/{id}")
    public Coincidencia actualizarCoincidencia(@PathVariable Long id, @Valid @RequestBody Coincidencia coincidenciaActualizada) {
        return coincidenciaRepository.findById(id).map(coincidencia -> {
            coincidencia.setEstado(coincidenciaActualizada.getEstado());
            return coincidenciaRepository.save(coincidencia);
        }).orElseThrow(() -> new com.dcknotfnd.ms_coincidencias.exception.ResourceNotFoundException("Coincidencia no encontrada"));
    }
   

    @DeleteMapping("/{id}")
    public void eliminarCoincidencia(@PathVariable Long id) {
        coincidenciaRepository.deleteById(id);
    }
}
/*
Aqui se busca la coincidencia base al id de la coincidencia
@param id: Identificador de la coincidencia a actualizar
*/