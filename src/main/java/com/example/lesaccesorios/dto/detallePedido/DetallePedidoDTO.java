package com.example.lesaccesorios.dto.detallePedido;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetallePedidoDTO {

    private int id_detalle_pedido;
    private int id_pedido;
    private int cantidad;
    private BigDecimal precio_unitario;
    private BigDecimal precio_total;

    private int id_producto;
    private String nombre_producto;

}
