package com.usuario.Ms_usuario.repository;

import com.usuario.Ms_usuario.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Añade esta línea para poder validar en el login
    Optional<Usuario> findByEmail(String email);
}