package com.alenvieira.pedidos.model;

import jakarta.persistence.*;

@Entity
@Table(name = "item")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo_item")
    private Long codigoItem;
    @Column(nullable = false)
    private String produto;
    @Column(nullable = false)
    private Long quantidade;
    @Column(nullable = false)
    private Double preco;

    public Item() {
    }

    public Item(String produto, Long quantidade, Double preco) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.preco = preco;
    }

    public Long getCodigoItem() {
        return codigoItem;
    }

    public String getProduto() {
        return produto;
    }

    public Long getQuantidade() {
        return quantidade;
    }

    public Double getPreco() {
        return preco;
    }

    @Override
    public String toString() {
        return "Item{" +
                "codigoItem=" + codigoItem +
                ", produto='" + produto + '\'' +
                ", quantidade=" + quantidade +
                ", preco=" + preco +
                '}';
    }
}
