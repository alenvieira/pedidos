package com.alenvieira.pedidos.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    @Query(value = "SELECT SUM(preco * quantidade) FROM pedido p, item i " +
            "WHERE p.codigo_pedido = ?1 AND p.codigo_pedido=i.codigo_pedido", nativeQuery = true)
    Optional<Double> consultarValorTotalPedido(Long codigoPedido);

    @Query(value = "SELECT COUNT(p.codigo_pedido) FROM pedido p " +
            "WHERE p.codigo_cliente = ?1 GROUP BY p.codigo_cliente", nativeQuery = true)
    Optional<Long> consultarQuantidadeDePedidoDoCliente(Long codigoCliente);

    Page<List<Pedido>> findAllByCodigoCliente(Long codigoCliente, Pageable pageable);
}
