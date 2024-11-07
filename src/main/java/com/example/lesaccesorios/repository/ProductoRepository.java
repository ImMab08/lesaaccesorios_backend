package com.example.lesaccesorios.repository;

import com.example.lesaccesorios.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    @Query(value = "SELECT * FROM producto WHERE id_producto = ?1", nativeQuery = true)
    Producto findByIdProducto(int id_producto);
}
