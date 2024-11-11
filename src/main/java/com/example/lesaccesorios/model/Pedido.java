package com.example.lesaccesorios.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Setter
@Entity
@Table(name = "pedido", schema = "lesaccesorios_db")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido", nullable = false)
    private Integer id;

    @Column(name = "fecha_pedido", nullable = false)
    private LocalDateTime fecha_pedido;

    @Column(name = "estado", nullable = false)
    private String estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario", nullable = false)
    @JsonIgnore
    private Usuario usuario;

    @OneToOne(mappedBy = "pedido", cascade = CascadeType.ALL)
    @JsonIgnore
    private Factura factura;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<DetallePedido> detallePedido;

    @OneToOne(mappedBy = "pedido", cascade = CascadeType.ALL)
    @JsonIgnore
    private Cliente cliente;

}
