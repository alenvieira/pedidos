package com.alenvieira.pedidos.listener;

import com.alenvieira.pedidos.model.Item;
import com.alenvieira.pedidos.model.Pedido;
import com.alenvieira.pedidos.model.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
class PedidoListenerTest {
    @Autowired
    private PedidoRepository repository;

    @Autowired
    private PedidoListener listener;

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("Deve receber o pedido da fila e salvar")
    public void deve_receber_o_pedido_da_fila_e_salvar() {
        // cenário
        Item lapis = new Item("lápis", 100L, 1.10);
        Item caderno = new Item("caderno", 10L, 1.00);
        Pedido pedido = new Pedido(1L, 1L, List.of(lapis, caderno));
        // ação
        listener.receiveMessage(pedido);

        List<Pedido> pedidos = repository.findAll();
        // validação
        assertEquals(pedidos.size(), 1);
        assertEquals(pedidos.get(0).getCodigoCliente(), pedido.getCodigoCliente());
    }

    @Test
    @DisplayName("Deve não salvar um pedido existente")
    public void deve_nao_salvar_pedido_existente() {
        // cenário
        Item lapis = new Item("lápis", 100L, 1.10);
        Item caderno = new Item("caderno", 10L, 1.00);
        Pedido pedido = new Pedido(1L, 1L, List.of(lapis, caderno));
        repository.save(pedido);
        // ação
        listener.receiveMessage(pedido);

        List<Pedido> pedidos = repository.findAll();
        // validação
        assertEquals(pedidos.size(), 1);
        assertEquals(pedidos.get(0).getCodigoCliente(), pedido.getCodigoCliente());
    }

    @Test
    @DisplayName("Deve não salvar um pedido inválido")
    public void deve_nao_salvar_um_pedido_invalido() {
        // cenário
        Item lapis = new Item("lápis", null, null);
        Pedido pedido = new Pedido(1L, null, List.of(lapis));
        // ação
        assertThrows(
                DataIntegrityViolationException.class, () -> {
                    listener.receiveMessage(pedido);
                }
        );

        List<Pedido> pedidos = repository.findAll();
        // validação
        assertEquals(pedidos.size(), 0);
    }

}