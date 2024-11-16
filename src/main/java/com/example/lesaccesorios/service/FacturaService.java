package com.example.lesaccesorios.service;

import com.example.lesaccesorios.dto.detallePedido.DetallePedidoDTO;
import com.example.lesaccesorios.dto.factura.FacturaDTO;
import com.example.lesaccesorios.model.*;
import com.example.lesaccesorios.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacturaService {

    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private FacturaRepository facturaRepository;
    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    // Obtener una factura por su ID.
    public FacturaDTO getFacturaById(Integer id_factura, Authentication authentication) {
        // Obtener al usuario autenticado
        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        Factura factura = facturaRepository.findById(id_factura)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada."));

        // Verificar si la factura pertenece al usuario autenticado.
        if (!factura.getUsuario().getId_usuario().equals(usuario.getId_usuario())) {
            throw new RuntimeException("Acceso denegado: La factura no pertenece a este usuario.");
        }

        // Crear DTO de factura
        FacturaDTO facturaDTO = new FacturaDTO();
        facturaDTO.setId_factura(factura.getId());
        facturaDTO.setId_usuario(usuario.getId_usuario());
        facturaDTO.setId_pedido(factura.getPedido().getId());
        facturaDTO.setFecha_factura(factura.getFecha_factura());
        facturaDTO.setTotal(factura.getTotal());

        // Obtener detalles del pedido
        List<DetallePedido> detallesPedido = detallePedidoRepository.findByPedidoId(factura.getPedido().getId());
        List<DetallePedidoDTO> detallesDTO = detallesPedido.stream().map(detalle -> {
            DetallePedidoDTO detalleDTO = new DetallePedidoDTO();
            detalleDTO.setId_detalle_pedido(detalle.getId());
            detalleDTO.setId_producto(detalle.getProducto().getId_producto());
            detalleDTO.setNombre_producto(detalle.getProducto().getNombre());
            detalleDTO.setCantidad(detalle.getCantidad());
            detalleDTO.setPrecio_unitario(detalle.getPrecio_unitario());
            detalleDTO.setPrecio_total(detalle.getPrecio_total());
            return detalleDTO;
        }).collect(Collectors.toList());

        facturaDTO.setDetalles(detallesDTO); // Asignar detalles al DTO de factura

        return facturaDTO;
    }

    // Crear una factura
    public Factura createFactura(FacturaDTO facturaDTO, Authentication authentication) {
        // Obtener usuario autenticado
        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Obtener pedido y verificar que pertenece al usuario autenticado
        int id_pedido = facturaDTO.getId_pedido();
        Pedido pedido = usuario.getPedido()
                .stream()
                .filter(p -> p.getId().equals(id_pedido))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        // Obtener los detalles del pedido desde la base de datos
        List<DetallePedido> detallesPedido = detallePedidoRepository.findByPedidoId(facturaDTO.getId_pedido());

        // Asignar cliente (si es necesario)
        Cliente cliente = null;
        if (facturaDTO.getId_cliente() != 0) {
            cliente = clienteRepository.findById(facturaDTO.getId_cliente())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        }

        // Calcular el total de la factura sumando el precio_total de cada detalle de pedido
        BigDecimal totalFactura = BigDecimal.ZERO;
        for (DetallePedido detalle : detallesPedido) {
            totalFactura = totalFactura.add(detalle.getPrecio_total());
        }

        // Crear factura
        Factura factura = new Factura();
        factura.setUsuario(usuario);  // Asociamos el usuario autenticado
        factura.setPedido(pedido);    // Asociamos el pedido
        factura.setFecha_factura(LocalDateTime.now());  // Fecha actual
        factura.setTotal(totalFactura);  // Total calculado

        // Guardar la factura en la base de datos
        return facturaRepository.save(factura);
    }

}
