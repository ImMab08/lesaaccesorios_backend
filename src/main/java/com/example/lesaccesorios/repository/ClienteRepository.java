package com.example.lesaccesorios.repository;

import com.example.lesaccesorios.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    @Query(value = "SELECT * FROM cliente WHERE id_cliente = ?1", nativeQuery = true)
    Cliente findClienteById(int id_cliente);
}
