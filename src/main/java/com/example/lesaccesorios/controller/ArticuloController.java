package com.example.lesaccesorios.controller;

import com.example.lesaccesorios.model.Articulo;
import com.example.lesaccesorios.service.ArticuloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(value = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("/v01/articulo/")
public class ArticuloController {
    @Autowired
    ArticuloService articuloService;

    // Traer las categorias del usuario
    @GetMapping("usuario")
    public List<Articulo> getCategoriasUsuarioByEmail(Authentication authentication) {
        return articuloService.getArticuloUsuario(authentication);
    }

    // Crear categoria del usuario
    @PostMapping("create")
    public ResponseEntity<?> createCategoriaUsuario(@RequestBody Articulo createArticulo, Authentication authentication) {
        try {
            Articulo newArticulo = articuloService.createArticuloUsuario(createArticulo, authentication);
            return new ResponseEntity<>(newArticulo, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Editar categoria
    @PutMapping("update/{id_articulo}")
    public ResponseEntity<?> updateArticulo(@PathVariable("id_articulo") int id_articulo, @RequestBody Articulo updateArticulo, Authentication authentication) {
        try {
            String articuloActualizad = articuloService.updateArticuloUsuario(id_articulo, updateArticulo, authentication);
            return new ResponseEntity<>(articuloActualizad, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Eliminar Categoria
    @DeleteMapping("delete/{id_articulo}")
    public String deleteCategoria(@PathVariable("id_articulo") int id_articulo, Authentication authentication) {
        return articuloService.deleteArticuloUsuario(id_articulo, authentication);
    }
}
