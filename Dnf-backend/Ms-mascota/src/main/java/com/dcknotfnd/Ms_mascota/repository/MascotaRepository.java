package com.dcknotfnd.Ms_mascota.repository;

import com.dcknotfnd.Ms_mascota.model.Mascota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MascotaRepository extends JpaRepository<Mascota, Long> {

    // El método que ya tenías para buscar las mascotas de un usuario específico
    List<Mascota> findByUsuarioId(Long usuarioId);

    // Búsqueda para el endpoint de Autocompletado (Sugerencias)
    // Busca cualquier mascota cuyo nombre contenga el texto (ignorando mayúsculas/minúsculas)
    List<Mascota> findByNameContainingIgnoreCase(String name);

    // Si un parámetro llega nulo desde el frontend, simplemente lo ignora.
    //con esto evitamos hacer mucho codigo en el frontend
    @Query("SELECT m FROM Mascota m WHERE " +
            "(:status = '' OR m.status = :status) AND " +
            "(:type = '' OR m.type = :type) AND " +
            "(:query = '' OR LOWER(m.name) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(m.location) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<Mascota> buscarConFiltros(@Param("status") String status,
                                   @Param("type") String type,
                                   @Param("query") String query);
}