package com.dcknotfnd.ms_coincidencias.repository;

import com.dcknotfnd.ms_coincidencias.model.Coincidencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoincidenciaRepository extends JpaRepository<Coincidencia, Long> {
}