package com.example.lesaccesorios.service;

import com.example.lesaccesorios.dto.detallePedido.DetallePedidoDTO;
import com.example.lesaccesorios.model.DetallePedido;
import com.example.lesaccesorios.model.Pedido;
import com.example.lesaccesorios.model.Producto;
import com.example.lesaccesorios.model.Usuario;
import com.example.lesaccesorios.repository.DetallePedidoRepository;
import com.example.lesaccesorios.repository.PedidoRepository;
import com.example.lesaccesorios.repository.ProductoRepository;
import com.example.lesaccesorios.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class DetallePedidoService {

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;
    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ProductoRepository productoRepository;

    // Obtener todos los detalles de un pedido por su ID
    public List<DetallePedido> getDetallesByPedidoId(Integer id_pedido, Authentication authentication) {
        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Pedido pedido = pedidoRepository.findById(id_pedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        // Verificar que el pedido pertenece al usuario autenticado
        if (!pedido.getUsuario().getId_usuario().equals(usuario.getId_usuario())) {
            throw new RuntimeException("Acceso denegado: no puedes ver los detalles de un pedido que no te pertenece");
        }

        return detallePedidoRepository.findByPedidoId(id_pedido); // Cambia a este método si lo descomentaste
    }

    // Crear nuevos detalles de pedido.
    public List<DetallePedido> createDetallesPedido(List<DetallePedidoDTO> detallesPedidoDTO, Authentication authentication) {
        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Buscamos el pedido correspondiente.
        int id_pedido = detallesPedidoDTO.get(0).getId_pedido(); // Suponiendo que todos los detalles son para el mismo pedido
        Pedido pedido = usuario.getPedido()
                .stream()
                .filter(p -> p.getId().equals(id_pedido))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        List<DetallePedido> detalles = new ArrayList<>();
        BigDecimal totalPedido = BigDecimal.ZERO; // Inicializamos el total del pedido

        for (DetallePedidoDTO detalleDTO : detallesPedidoDTO) {
            // Buscamos el producto directamente desde el repositorio.
            Producto producto = productoRepository.findById(detalleDTO.getId_producto())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            // Crear un nuevo objeto de DetallePedido a partir del DetallePedidoDTO
            DetallePedido detallePedido = new DetallePedido();
            detallePedido.setCantidad(detalleDTO.getCantidad());
            detallePedido.setPrecio_unitario(producto.getPrecio());
            detallePedido.setPrecio_total(producto.getPrecio().multiply(BigDecimal.valueOf(detalleDTO.getCantidad())));
            detallePedido.setPedido(pedido);
            detallePedido.setProducto(producto);

            // Añadir el detalle a la lista
            detalles.add(detallePedido);

            // Sumar al total del pedido
            totalPedido = totalPedido.add(detallePedido.getPrecio_total());
        }

        // Guardar todos los detalles del pedido en el repositorio.
        detallePedidoRepository.saveAll(detalles);

        // Aquí podrías retornar el total del pedido o manejarlo según lo necesites.
        return detalles; // o puedes devolver un objeto que incluya detalles y el total
    }

    // Eliminar un detalle de pedido
    public void deleteDetallePedido(Integer id_detalle_pedido, Authentication authentication) {
        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        DetallePedido detallePedido = detallePedidoRepository.findById(id_detalle_pedido)
                .orElseThrow(() -> new RuntimeException("Detalle de pedido no encontrado"));

        if (!detallePedido.getPedido().getUsuario().getId_usuario().equals(usuario.getId_usuario())) {
            throw new RuntimeException("Acceso denegado: no puedes eliminar un detalle de pedido que no te pertenece");
        }

        detallePedidoRepository.delete(detallePedido);
    }

}
