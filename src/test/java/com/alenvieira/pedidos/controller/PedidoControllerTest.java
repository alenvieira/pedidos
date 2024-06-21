package com.alenvieira.pedidos.controller;

import com.alenvieira.pedidos.model.Item;
import com.alenvieira.pedidos.model.Pedido;
import com.alenvieira.pedidos.model.PedidoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(printOnlyOnFailure = false)
class PedidoControllerTest {

    @Autowired
    private PedidoRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    protected MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("Deve retornar o valor total do pedido")
    public void deve_retornar_o_valor_total_do_pedido() throws Exception {
        // cenário
        Item lapis = new Item("lápis", 100L, 1.10);
        Item caderno = new Item("caderno", 10L, 1.00);
        Pedido pedido = new Pedido(1L, 1L, List.of(lapis, caderno));
        repository.save(pedido);
        String endPoint = String.format("/api/v1/pedidos/%d/valor-total", pedido.getCodigoPedido());
        // ação
        String response = mockMvc.perform(get(endPoint))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(UTF_8);

        ValorTotalPedidoResponse cursoConsulta = objectMapper.readValue(response, ValorTotalPedidoResponse.class);
        // validação
        assertEquals(cursoConsulta.codigoPedido(), pedido.getCodigoPedido());
        assertEquals(cursoConsulta.valorTotal(), 120, 0.000001d);
    }

    @Test
    @DisplayName("Deve retornar um erro de pedido inexistente")
    public void teste_retornar_erro_pedido_inexistente() throws Exception {
        // cenário
        String endPoint = "/api/v1/pedidos/-1/valor-total";
        // ação
        MockHttpServletResponse response = mockMvc.perform(get(endPoint))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse();
        // validação
        assertEquals(response.getErrorMessage(), "Pedido não encontrado.");
    }

    @Test
    @DisplayName("Deveria retornar erro para quantidade de pedido")
    public void deve_retornar_erro_para_quantidade_de_pedido() throws Exception {
        // cenário
        String endPoint = "/api/v1/pedidos/clientes/1/quantidade-de-pedido";
        // ação
        MockHttpServletResponse response = mockMvc.perform(get(endPoint))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse();
        // validação
        assertEquals(response.getErrorMessage(), "Pedidos não encontrado.");
    }

    @Test
    @DisplayName("Deveria retornar a quantidade de pedidos do cliente")
    public void deve_retornar_a_quantidade_de_pedidos_do_cliente() throws Exception {
        // cenário
        Item caneta = new Item("caneta", 20L, 2.00);
        Pedido pedido1 = new Pedido(2L, 1L, List.of(caneta));
        Item lapis = new Item("lápis", 100L, 1.10);
        Item caderno = new Item("caderno", 10L, 1.00);
        Pedido pedido2 = new Pedido(3L, 1L, List.of(lapis, caderno));
        repository.save(pedido1);
        repository.save(pedido2);
        String endPoint = "/api/v1/pedidos/clientes/1/quantidade-de-pedido";
        // ação
        String response = mockMvc.perform(get(endPoint))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(UTF_8);

        QuantidadeDePedidoDoClienteResponse quantidadeDePedidoDoClienteResponse = objectMapper
                .readValue(response, QuantidadeDePedidoDoClienteResponse.class);
        // validação
        assertEquals(quantidadeDePedidoDoClienteResponse.codigoCliente(), 1);
        assertEquals(quantidadeDePedidoDoClienteResponse.quantidadeDePedido(), 2);
    }

    @Test
    @DisplayName("deveria retornar uma lista com os pedidos do cliente")
    public void deve_retornar_uma_lista_com_os_pedidos_do_cliente() throws Exception {
        // cenário
        Item caneta = new Item("caneta", 20L, 2.00);
        Pedido pedido1 = new Pedido(4L, 1L, List.of(caneta));
        Item lapis = new Item("lápis", 100L, 1.10);
        Pedido pedido2 = new Pedido(5L, 1L, List.of(lapis));
        Item caderno = new Item("caderno", 10L, 1.00);
        Pedido pedido3 = new Pedido(6L, 1L, List.of(caderno));
        repository.save(pedido1);
        repository.save(pedido2);
        repository.save(pedido3);
        String endPoint = "/api/v1/pedidos/clientes/1";
        // ação e validação
        mockMvc.perform(get(endPoint))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.content.length()").value(3));
    }

    @Test
    @DisplayName("Deveria retornar uma lista vazia de pedidos")
    public void deve_retornar_uma_lista_vazia_de_pedidos() throws Exception {
        // cenário
        String endPoint = "/api/v1/pedidos/clientes/1";
        // ação e validação
        mockMvc.perform(get(endPoint))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages").value(0))
                .andExpect(jsonPath("$.totalElements").value(0))
                .andExpect(jsonPath("$.content").isEmpty());
    }
}