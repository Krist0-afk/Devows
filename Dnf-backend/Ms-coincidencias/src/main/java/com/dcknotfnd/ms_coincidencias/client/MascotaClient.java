package com.dcknotfnd.ms_coincidencias.client;

import com.dcknotfnd.ms_coincidencias.dto.MascotaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

// Quitamos la 'url' estática, Eureka se encargará de buscar 'ms-mascota'
@FeignClient(name = "ms-mascota")
public interface MascotaClient {
    @GetMapping("/api/pets")
    List<MascotaDTO> obtenerTodasLasMascotas();
}