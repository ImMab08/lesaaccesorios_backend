package com.example.lesaccesorios.repository;

import com.example.lesaccesorios.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    @Query(value = "SELECT * FROM usuario WHERE email = ?1", nativeQuery = true)
    Optional<Usuario> findByEmail(String email);
}
