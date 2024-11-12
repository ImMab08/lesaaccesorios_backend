package com.example.lesaccesorios.dto.factura;

import com.example.lesaccesorios.dto.detallePedido.DetallePedidoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacturaDTO {

    private int id_factura;
    private int id_usuario;
    private LocalDateTime fecha_factura;
    private BigDecimal total;

    private int id_pedido;
    private List<DetallePedidoDTO> detalles;

    private int id_cliente;
    private String nombre_cliente;
    private String apellido_cliente;

}
