package com.example.lesaccesorios.service;

import com.example.lesaccesorios.dto.producto.ProductoDTO;
import com.example.lesaccesorios.model.Articulo;
import com.example.lesaccesorios.model.Producto;
import com.example.lesaccesorios.model.Usuario;
import com.example.lesaccesorios.repository.ArticuloRepository;
import com.example.lesaccesorios.repository.ProductoRepository;
import com.example.lesaccesorios.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductoService {

    @Autowired
    ProductoRepository productoRepository;
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    ArticuloRepository articuloRepository;

    // Traer productos
    public List<ProductoDTO> getProducto(Authentication authentication) {
        String email = authentication.getName();

        // Obtenemos los productos asociados al usuario
        List<Producto> productos = productoRepository.findByUsuarioEmail(email);
        List<ProductoDTO> productosDTO = new ArrayList<>();

        // Convertimos cada producto a ProductoDTO
        for (Producto producto : productos) {
            productosDTO.add(new ProductoDTO(
                    producto.getId_producto(),
                    producto.getNombre(),
                    producto.getDescripcion(),
                    producto.getPrecio(),
                    producto.getStock(),
                    producto.getImagen(),
                    producto.getArticulo().getId(),  // Obtener el id del artículo
                    producto.getArticulo().getNombre()  // Obtener el nombre del artículo
            ));
        }

        return productosDTO;
    }


    public Producto createProducto(ProductoDTO productoDTO, Authentication authentication) {
        // Obtener el usuario autenticado
        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Buscar el artículo en la base de datos
        Articulo articulo = articuloRepository.findById(productoDTO.getId_articulo())
                .orElseThrow(() -> new RuntimeException("Artículo no encontrado"));

        // Crear el producto
        Producto createProducto = new Producto();
        createProducto.setNombre(productoDTO.getNombre());
        createProducto.setDescripcion(productoDTO.getDescripcion());
        createProducto.setPrecio(productoDTO.getPrecio());
        createProducto.setStock(productoDTO.getStock());
        createProducto.setImagen(productoDTO.getImagen());
        createProducto.setArticulo(articulo);
        createProducto.setUsuario(usuario);

        // Guardar y devolver el producto
        return productoRepository.save(createProducto);
    }


    // Editar producto
    public Producto updateProducto(int id_producto, ProductoDTO productoDTO, Authentication authentication) {
        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Producto updateProducto = productoRepository.findByIdProducto(id_producto);

        // Verifica si el producto tiene un usuario asociado y si coincide con el usuario autenticado
        if (updateProducto.getUsuario() == null || !updateProducto.getUsuario().equals(usuario)) {
            throw new RuntimeException("No tienes permisos para editar este producto");
        }

        Articulo articulo = articuloRepository.findById(productoDTO.getId_articulo())
                .orElseThrow(() -> new RuntimeException("Categoria no encontrado"));

        updateProducto.setNombre(productoDTO.getNombre());
        updateProducto.setDescripcion(productoDTO.getDescripcion());
        updateProducto.setPrecio(productoDTO.getPrecio());
        updateProducto.setStock(productoDTO.getStock());
        updateProducto.setImagen(productoDTO.getImagen());
        updateProducto.setArticulo(articulo);

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
