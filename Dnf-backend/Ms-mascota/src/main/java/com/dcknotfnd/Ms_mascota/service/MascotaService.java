package com.dcknotfnd.Ms_mascota.service;

import com.dcknotfnd.Ms_mascota.model.Mascota;
import com.dcknotfnd.Ms_mascota.repository.MascotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MascotaService {

    @Autowired
    private MascotaRepository mascotaRepository;

    @Cacheable(value = "mascotas", key = "#id")
    public Optional<Mascota> obtenerMascotaPorId(Long id) {
        return mascotaRepository.findById(id);
    }

    public List<Mascota> obtenerMascotas(String status, String type, String query) {
        String safeStatus = (status != null) ? status : "";
        String safeType = (type != null) ? type : "";
        String safeQuery = (query != null) ? query : "";
        return mascotaRepository.buscarConFiltros(safeStatus, safeType, safeQuery);
    }
}
