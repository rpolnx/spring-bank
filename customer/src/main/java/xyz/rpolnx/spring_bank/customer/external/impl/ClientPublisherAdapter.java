package xyz.rpolnx.spring_bank.customer.external.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import xyz.rpolnx.spring_bank.customer.external.ClientPublisher;
import xyz.rpolnx.spring_bank.customer.model.dto.ClientEvent;

@Component
@RequiredArgsConstructor
public class ClientPublisherAdapter implements ClientPublisher {
    private final AmqpTemplate amqpTemplate;

    @Value("${customer-queue-name}")
    private String routingKey;

    @SneakyThrows
    @Override
    public void handleClientEvent(ClientEvent event) {
        amqpTemplate.convertAndSend(routingKey, event);
    }
}
