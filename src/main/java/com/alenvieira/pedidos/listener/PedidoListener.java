package com.alenvieira.pedidos.listener;

import com.alenvieira.pedidos.model.Pedido;
import com.alenvieira.pedidos.model.PedidoRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PedidoListener {

    private PedidoRepository repository;

    public PedidoListener(PedidoRepository pedidoRepository) {
        this.repository = pedidoRepository;
    }

    @Transactional
    @RabbitListener(queues = {RabbitMQConfig.queue})
    public void receiveMessage(Pedido pedido) {
        repository.save(pedido);
    }
}
