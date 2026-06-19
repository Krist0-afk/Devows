package com.dcknotfnd.ms_coincidencias.service;

import com.dcknotfnd.ms_coincidencias.client.MascotaClient;
import com.dcknotfnd.ms_coincidencias.dto.MascotaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CoincidenciaService {

    @Autowired
    private MascotaClient mascotaClient;

    @Cacheable(value = "coincidencias", key = "#especie + #color")
    public List<MascotaDTO> buscarCoincidencias(String especie, String color) {
        List<MascotaDTO> todasLasMascotas = mascotaClient.obtenerTodasLasMascotas();
        return todasLasMascotas.stream()
                .filter(m -> m.getType().equalsIgnoreCase(especie) &&
                        m.getBreed().equalsIgnoreCase(color))
                .collect(Collectors.toList());
    }
}
