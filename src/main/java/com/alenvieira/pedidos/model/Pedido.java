package com.alenvieira.pedidos.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedido",
        indexes = {
                @Index(name = "codigo_cliente_index", columnList = "codigo_cliente")
        })
public class Pedido {
    @Id
    @Column(name = "codigo_pedido")
    private Long codigoPedido;
    @Column(name = "codigo_cliente", nullable = false)
    private Long codigoCliente;
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    @JoinColumn(name = "codigo_pedido", foreignKey = @ForeignKey(name = "codigo_pedido_fk"))
    private List<Item> itens = new ArrayList<>();

    public Pedido() {
    }

    public Pedido(Long codigoPedido, Long codigoCliente, List<Item> itens) {
        this.codigoPedido = codigoPedido;
        this.codigoCliente = codigoCliente;
        this.itens = itens;
    }

    public Long getCodigoPedido() {
        return codigoPedido;
    }

    public Long getCodigoCliente() {
        return codigoCliente;
    }

    public List<Item> getItens() {
        return itens;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "codigoPedido=" + codigoPedido +
                ", codigoCliente=" + codigoCliente +
                ", itens=" + itens +
                '}';
    }
}
