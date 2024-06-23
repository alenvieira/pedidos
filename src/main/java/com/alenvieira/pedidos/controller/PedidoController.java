package com.alenvieira.pedidos.controller;

import com.alenvieira.pedidos.model.Pedido;
import com.alenvieira.pedidos.model.PedidoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/api/v1/pedidos")
public class PedidoController {
    private PedidoRepository repository;

    public PedidoController(PedidoRepository pedidoRepository) {
        this.repository = pedidoRepository;
    }

    @GetMapping("/{codigo-pedido}/valor-total")
    public ResponseEntity<?> calcularValorTotalPedido(@PathVariable("codigo-pedido") Long codigoPedido) {
        Double valorTotal = this.repository.consultarValorTotalPedido(codigoPedido).orElseThrow(
                () -> new ResponseStatusException(
                        NOT_FOUND, "Pedido não encontrado."
                )
        );
        return ResponseEntity.ok(new ValorTotalPedidoResponse(codigoPedido, valorTotal));
    }

    @GetMapping("/clientes/{codigo-cliente}/quantidade-de-pedido")
    public ResponseEntity<?> calcularQuantidadeDePedidoPorCliente(@PathVariable("codigo-cliente") Long codigoCliente) {
        Long quantidadeDePedido = this.repository.consultarQuantidadeDePedidoDoCliente(codigoCliente).orElseThrow(
                () -> new ResponseStatusException(
                        NOT_FOUND, "Pedidos não encontrado."
                )
        );
        return ResponseEntity.ok(new QuantidadeDePedidoDoClienteResponse(codigoCliente, quantidadeDePedido));
    }

    @GetMapping("/clientes/{codigo-cliente}")
    public ResponseEntity<?> consultaTodosPedidosDoCliente(@PathVariable("codigo-cliente") Long codigoCliente,
                                                           @RequestParam(defaultValue = "0") int numeroDaPagina,
                                                           @RequestParam(defaultValue = "10") int tamanhoDaPagina) {
        Page<List<Pedido>> pedidos = this.repository
                .findAllByCodigoCliente(codigoCliente, PageRequest.of(numeroDaPagina, tamanhoDaPagina));
        return ResponseEntity.ok(pedidos);
    }

}
