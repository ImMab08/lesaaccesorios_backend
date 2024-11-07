package com.example.lesaccesorios.service;

import com.example.lesaccesorios.model.Producto;
import com.example.lesaccesorios.model.Usuario;
import com.example.lesaccesorios.repository.ProductoRepository;
import com.example.lesaccesorios.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    @Autowired
    ProductoRepository productoRepository;
    @Autowired
    UsuarioRepository usuarioRepository;

    // Traer productos
    public List<Producto> getProducto(Authentication authentication) {
        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Producto> productos = usuario.getProducto();
        return productos;
    }

    // Crear producto
    public Producto createProducto(Producto producto, Authentication authentication) {
        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Producto createProducto = new Producto();
        createProducto.setNombre(producto.getNombre());
        createProducto.setDescripcion(producto.getDescripcion());
        createProducto.setPrecio(producto.getPrecio());
        createProducto.setStock(producto.getStock());
        createProducto.setImagen(producto.getImagen());

        createProducto.setUsuario(usuario);

        return productoRepository.save(createProducto);
    }

    // Editar producto
    public Producto updateProducto(int id_producto, Producto producto, Authentication authentication) {
        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Producto updateProducto = productoRepository.findByIdProducto(id_producto);

        // Verifica si el producto tiene un usuario asociado y si coincide con el usuario autenticado
        if (updateProducto.getUsuario() == null || !updateProducto.getUsuario().equals(usuario)) {
            throw new RuntimeException("No tienes permisos para editar este producto");
        }

        updateProducto.setNombre(producto.getNombre());
        updateProducto.setDescripcion(producto.getDescripcion());
        updateProducto.setPrecio(producto.getPrecio());
        updateProducto.setStock(producto.getStock());
        updateProducto.setImagen(producto.getImagen());

        return productoRepository.save(updateProducto);
    }

    // Eliminar producto
    public String deleteProducto(int id_producto, Authentication authentication) {
        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Producto producto = productoRepository.findByIdProducto(id_producto);

        if(!producto.getUsuario().equals(usuario)) {
            throw new RuntimeException("Producto no encontrado");
        }

        productoRepository.delete(producto);
        return "Producto eliminado con éxito";
    }
}
