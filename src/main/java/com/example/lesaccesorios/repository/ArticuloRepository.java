package com.example.lesaccesorios.repository;

import com.example.lesaccesorios.model.Articulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticuloRepository extends JpaRepository<Articulo, Integer> {
    @Query(value = "SELECT * FROM articulo WHERE nombre = ?1", nativeQuery = true)
    Articulo findArticuloByNombre(String nombre);
}
