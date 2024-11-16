package com.example.lesaccesorios.repository;

import com.example.lesaccesorios.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    @Query(value = "SELECT * FROM producto WHERE id_producto = ?1", nativeQuery = true)
    Producto findByIdProducto(int id_producto);

    @Query("SELECT p FROM Producto p JOIN FETCH p.articulo a WHERE p.usuario.email = :email")
    List<Producto> findByUsuarioEmail(@Param("email") String email);
}
