package com.example.lesaccesorios.service;

import com.example.lesaccesorios.model.Articulo;
import com.example.lesaccesorios.model.Usuario;
import com.example.lesaccesorios.repository.ArticuloRepository;
import com.example.lesaccesorios.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticuloService {

    @Autowired
    ArticuloRepository articuloRepository;
    @Autowired
    UsuarioRepository usuarioRepository;

    // Traer todos los articulos del usuario.
    public List<Articulo> getArticuloUsuario(Authentication authentication) {
        //  Enlistar categorías del usuario authenticado.
        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Articulo> articulos = usuario.getArticulo();
        return articulos;
    }

    // Crear un articulo para el usuario.
    public Articulo createArticuloUsuario(Articulo createArticulo, Authentication authentication) {
        String email = authentication.getName();
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);

        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();

            createArticulo.setUsuario(usuario);
            return articuloRepository.save(createArticulo);
        } else {
            throw new RuntimeException("Usuario no encontrado");
        }
    }

    // Actualizar articulo del usuario
    public String updateArticuloUsuario(int id_articulo, Articulo updateArticulo, Authentication authentication) {
        String email = authentication.getName();
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);

        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();

            // Buscar el articulo en la lista de las categorias del usuario.
            Articulo articulo =  usuario.getArticulo()
                    .stream() // Iterar entre las categoria.
                    .filter(idArticulo -> idArticulo.getId() == id_articulo) // Buscamos la categoria con el ID enviado.
                    .findFirst() // Recuperar la primera coincidencia.
                    .orElseThrow(() -> new RuntimeException("Articulo no encontrada o no pertenece a este usuario")); // Lanzamos una excepción en caso de error.

            // Actualizar campos de la cateogira.
            articulo.setNombre(updateArticulo.getNombre());
            articulo.setDescripcion(updateArticulo.getDescripcion());

            // Guardar la categoria actualizada en la db.
            articuloRepository.save(articulo);
            return "Articulo actualizada exitosamente.";
        } else {
            throw new RuntimeException("Usuario no encontrado");
        }
    }

    // Eliminar articulo del usuario
    public String deleteArticuloUsuario(int id_articulo, Authentication authentication) {
        String email = authentication.getName();
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);

        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();

            // Buscar la categoria en la lista de las categorias del usuario.
            Articulo categoria =  usuario.getArticulo()
                    .stream() // Iterar entre las categoria.
                    .filter(idArticulo -> idArticulo.getId() == id_articulo) // Buscamos la categoria con el ID enviado.
                    .findFirst() // Recuperar la primera coincidencia.
                    .orElseThrow(() -> new RuntimeException("Articulo no encontrada o no pertenece a este usuario")); // Lanzamos una excepción en caso de error.

            // Eliminar la categoria de la lista del usuario.
            usuario.getArticulo().remove(categoria);

            // Eliminar la categoria de la bd.
            articuloRepository.delete(categoria);
            return "Categoria eliminado exitosamente.";
        } else {
            throw new RuntimeException("Usuario no encontrado");
        }
    }

}
