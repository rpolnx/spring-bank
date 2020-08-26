package xyz.rpolnx.spring_bank.customer.external.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import xyz.rpolnx.spring_bank.customer.external.ClientPublisher;
import xyz.rpolnx.spring_bank.customer.model.dto.ClientDTO;

@Component
@RequiredArgsConstructor
@Log4j2
public class ClientPublisherAdapter implements ClientPublisher {
    private final AmqpTemplate amqpTemplate;
    private final ObjectMapper mapper;

    @Value("${customer-queue-name:customer-queue}")
    private String queue;

    @SneakyThrows
    @Override
    public void handleClientCreation(ClientDTO clientDTO) {
        amqpTemplate.convertAndSend(queue, mapper.writeValueAsString(clientDTO));
    }
}
