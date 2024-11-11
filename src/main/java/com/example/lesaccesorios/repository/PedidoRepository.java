package com.example.lesaccesorios.repository;

import com.example.lesaccesorios.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    @Query(value = "SELECT * FROM pedido WHERE id_pedido = ?1", nativeQuery = true)
    Pedido findPedidoById(int id_pedido);
}
