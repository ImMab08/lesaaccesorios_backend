package com.example.lesaccesorios.repository;

import com.example.lesaccesorios.model.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Integer> {

    @Query(value = "SELECT * FROM detalle_pedido WHERE id_detalle_pedido = ?1", nativeQuery = true)
    DetallePedido findDetallePedidoId(int id_detalle_pedido);

    List<DetallePedido> findByPedidoId(int id_pedido);
    void deleteByPedido_Id(int id_pedido);

}
