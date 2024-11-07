package com.example.lesaccesorios.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.example.lesaccesorios.model.Producto;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import com.example.lesaccesorios.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@CrossOrigin(value = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("/v01/producto/")
public class ProductoController {

    @Autowired
    ProductoService productoService;

    // Traer productos
    @GetMapping("usuario")
    public ResponseEntity<?> getProductoInfo(Authentication authentication) {
        try {
            List<Producto> getProductoInfo = productoService.getProducto(authentication);
            return ResponseEntity.ok(getProductoInfo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener la información de los productos del usuario");
        }
    }

    // Crear un producto
    @PostMapping("create")
    public ResponseEntity<?> createProducto(@RequestBody Producto producto, Authentication authentication) {
        try {
            Producto createProducto = productoService.createProducto(producto, authentication);
            return new ResponseEntity<>(createProducto, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el producto");
        }
    }

    // Editar un producto
    @PutMapping("update/{id_producto}")
    public ResponseEntity<?> updateProducto(@PathVariable("id_producto") int id_producto, @RequestBody Producto producto, Authentication authentication) {
        try {
            Producto updateProducto = productoService.updateProducto(id_producto, producto, authentication);
            return new ResponseEntity<>(updateProducto, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al editar el producto");
        }
    }

    // Eliminar un producto
    @DeleteMapping("delete/{id_producto}")
    public ResponseEntity<?> deleteProducto(@PathVariable("id_producto") int id_producto, Authentication authentication) {
        try {
            String deleteProducto = productoService.deleteProducto(id_producto, authentication);
            return new ResponseEntity<>(deleteProducto, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el producto");
        }
    }

}
