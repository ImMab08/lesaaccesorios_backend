package com.example.lesaccesorios.dto.pedido;

import com.example.lesaccesorios.dto.detallePedido.DetallePedidoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {

    private int id_pedido;
    private int id_usuario;
    private LocalDateTime fecha_pedido;
    private String estado;

    private List<DetallePedidoDTO> detalles;

}
