package com.comunidad.Ms_comunidad.repository;

import com.comunidad.Ms_comunidad.model.Historia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoriaRepository extends JpaRepository<Historia, Long> {
}