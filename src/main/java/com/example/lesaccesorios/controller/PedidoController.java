package com.example.lesaccesorios.controller;

import com.example.lesaccesorios.dto.pedido.PedidoDTO;
import com.example.lesaccesorios.model.Pedido;
import com.example.lesaccesorios.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(value = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("/v01/pedido/")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    // Obtener un pedido por su ID
    @GetMapping("{id}")
    public ResponseEntity<?> getPedido(@PathVariable Integer id, Authentication authentication) {
        try {
            PedidoDTO pedidoDTO = pedidoService.getPedidoById(id, authentication);
            return new ResponseEntity<>(pedidoDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // Crear un nuevo pedido
    @PostMapping("create")
    public ResponseEntity<?> createPedido(@RequestBody PedidoDTO createPedido, Authentication authentication) {
        try {
            Pedido newPedido = pedidoService.createPedido(createPedido, authentication);
            return new ResponseEntity<>(newPedido, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Eliminar un pedido
    @DeleteMapping("delete/{id_pedido}")
    public String deletePedido(@PathVariable("id_pedido") int id_pedido, Authentication authentication) {
        return pedidoService.deletePedido(id_pedido, authentication);
    }

}
