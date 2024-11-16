package com.example.lesaccesorios.dto.pedido;

import com.example.lesaccesorios.dto.detallePedido.DetallePedidoDTO;
import com.example.lesaccesorios.dto.producto.ProductoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {

    private int id_pedido;
    private int id_usuario;
    private LocalDateTime fecha_pedido;

    private int id_cliente;
    private String nombreCliente;
    private String apelldioCliente;
    private String emailCliente;
    private String direccionCliente;
    private String telefonoCliente;

    List<DetallePedidoDTO> detallePedidoDTOS;
}
