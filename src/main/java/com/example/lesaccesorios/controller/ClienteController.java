package com.example.lesaccesorios.controller;

import com.example.lesaccesorios.model.Cliente;
import com.example.lesaccesorios.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(value = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("/v01/cliente/")
public class ClienteController {

    @Autowired
    ClienteService clienteService;

    // Traer productos
    @GetMapping("usuario")
    public ResponseEntity<?> getProductoInfo(Authentication authentication) {
        try {
            List<Cliente> getClienteInfo = clienteService.getCliente(authentication);
            return ResponseEntity.ok(getClienteInfo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener la información de los productos del usuario");
        }
    }

    // Crear un producto
    @PostMapping("create")
    public ResponseEntity<?> createCliente(@RequestBody Cliente cliente, Authentication authentication) {
        try {
            Cliente createProducto = clienteService.createCliente(cliente, authentication);
            return new ResponseEntity<>(createProducto, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el cliente");
        }
    }

    // Editar un producto
    @PutMapping("update/{id_cliente}")
    public ResponseEntity<?> updateProducto(@PathVariable("id_cliente") int id_cliente, @RequestBody Cliente cliente, Authentication authentication) {
        try {
            Cliente updateCliente = clienteService.updateCliente(id_cliente, cliente, authentication);
            return new ResponseEntity<>(updateCliente, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al editar el producto");
        }
    }

    // Eliminar un producto
    @DeleteMapping("delete/{id_cliente}")
    public ResponseEntity<?> deleteCliente(@PathVariable("id_cliente") int id_cliente, Authentication authentication) {
        try {
            String deleteProducto = clienteService.deleteCliente(id_cliente, authentication);
            return new ResponseEntity<>(deleteProducto, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el producto");
        }
    }

}
