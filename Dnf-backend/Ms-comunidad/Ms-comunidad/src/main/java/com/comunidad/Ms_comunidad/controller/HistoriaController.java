package com.comunidad.Ms_comunidad.controller;

import com.comunidad.Ms_comunidad.model.Historia;
import com.comunidad.Ms_comunidad.repository.HistoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/success-stories")
public class HistoriaController {

    @Autowired
    private HistoriaRepository historiaRepository;

    @GetMapping
    @Cacheable(value = "historias", key = "'all'")
    public List<Historia> obtenerHistorias() {
        return historiaRepository.findAll();
    }

    @PostMapping
    @CacheEvict(value = "historias", key = "'all'")
    public Historia crearHistoria(@RequestBody Historia historia) {
        System.out.println("Creando historia: " + historia.getTitle());
        Historia nuevaHistoria = historiaRepository.save(historia);
        System.out.println("Historia guardada: " + nuevaHistoria.getId());
        return nuevaHistoria;
    }
}
/*
En historiaController se define la gestion de historias, como obtener las historias registradas
y crear una nueva historia de exito, que se muestra en el frontend.

@param historia: Objeto que contiene la información de la historia a crear (nombre del dueño, nombre de la mascota, historia de exito)
@return: La historia creada con su información registrada en la base de datos
*/