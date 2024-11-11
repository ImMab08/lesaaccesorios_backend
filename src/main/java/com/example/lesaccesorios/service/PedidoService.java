package com.example.lesaccesorios.service;

import com.example.lesaccesorios.dto.detallePedido.DetallePedidoDTO;
import com.example.lesaccesorios.dto.pedido.PedidoDTO;
import com.example.lesaccesorios.model.Pedido;
import com.example.lesaccesorios.model.Usuario;
import com.example.lesaccesorios.repository.DetallePedidoRepository;
import com.example.lesaccesorios.repository.PedidoRepository;
import com.example.lesaccesorios.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PedidoService {
    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    // Obtener un pedido por su ID y verificar que pertenece al usuario autenticado
    public PedidoDTO getPedidoById(Integer id_pedido, Authentication authentication) {
        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Pedido pedido = pedidoRepository.findById(id_pedido)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado"));

        // Verificar que el pedido pertenece al usuario autenticado
        if (!pedido.getUsuario().getId_usuario().equals(usuario.getId_usuario())) {
            throw new RuntimeException("Acceso denegado: el pedido no pertenece al usuario autenticado");
        }

        // Crear el PedidoDTO
        PedidoDTO pedidoDTO = new PedidoDTO();
        pedidoDTO.setId_pedido(pedido.getId());
        pedidoDTO.setId_usuario(usuario.getId_usuario());
        pedidoDTO.setFecha_pedido(pedido.getFecha_pedido());
        pedidoDTO.setEstado(pedido.getEstado());

        // Obtener los detalles del pedido y convertirlos a DetallePedidoDTO
        List<DetallePedidoDTO> detallesDTO = pedido.getDetallePedido()
                .stream()
                .map(detalle -> new DetallePedidoDTO(
                        detalle.getId(),
                        detalle.getPedido().getId(),
                        detalle.getCantidad(),
                        detalle.getPrecio_unitario(),
                        detalle.getPrecio_total(),
                        detalle.getProducto().getId_producto(),
                        detalle.getProducto().getNombre()
                ))
                .collect(Collectors.toList());

        // Asignar los detalles al PedidoDTO
        pedidoDTO.setDetalles(detallesDTO);

        return pedidoDTO;
    }

    // Crear un nuevo pedido
    public Pedido createPedido(PedidoDTO pedidoDTO, Authentication authentication) {
        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Crear un nuevo objeto de Pedido a partir del PedidoDTO
        Pedido pedido = new Pedido();
        pedido.setFecha_pedido(LocalDateTime.now());
        pedido.setEstado(pedidoDTO.getEstado());
        pedido.setUsuario(usuario);

        // Guardar el pedido en el repositorio
        Pedido nuevoPedido = pedidoRepository.save(pedido);

        // Puedes retornar el PedidoDTO si es necesario para el frontend
        return nuevoPedido;
    }

    // Eliminar un pedido y los detalles de un pedido
    @Transactional
    public String deletePedido(int id_pedido, Authentication authentication) {
        String email = authentication.getName();
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);

        if (!usuarioOptional.isPresent()) {
            throw new RuntimeException("Usuario no encontrado");
        }

        Usuario usuario = usuarioOptional.get();

        // Buscar el pedido en la lista de pedidos del usuario
        Pedido pedido = usuario.getPedido()
                .stream()
                .filter(idPedido -> idPedido.getId() == id_pedido)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        // Validar que el pedido pertenece al usuario
        if (!pedido.getUsuario().getId_usuario().equals(usuario.getId_usuario())) {
            throw new RuntimeException("Acceso denegado: no puedes eliminar un pedido que no te pertenece");
        }

        try {
            // Eliminar todos los detallespedido asociados al pedido
            detallePedidoRepository.deleteByPedido_Id(pedido.getId());

            // Eliminar el pedido de la lista de pedidos del usuario
            usuario.getPedido().remove(pedido);

            // Eliminar el pedido de la base de datos
            pedidoRepository.delete(pedido);

            return "Pedido y detalles del pedido eliminados exitosamente.";
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el pedido: " + e.getMessage());
        }
    }
}
