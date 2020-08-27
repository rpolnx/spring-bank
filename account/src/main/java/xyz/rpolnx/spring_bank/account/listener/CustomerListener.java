package xyz.rpolnx.spring_bank.account.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import xyz.rpolnx.spring_bank.account.model.dto.CustomerEvent;
import xyz.rpolnx.spring_bank.account.model.enums.EventHandler;
import xyz.rpolnx.spring_bank.account.service.AccountService;

@Component
@RequiredArgsConstructor
@Log4j2
public class CustomerListener {
    private final AccountService service;

    @RabbitListener(queues = "${customer-queue-name}")
    public void receive(@Payload final CustomerEvent event) {
        log.info("Consuming message from customer queue: {}", event);

        EventHandler eventHandler = EventHandler.fromEventType(event.getType());

        eventHandler.getCallable().accept(service, event);

        log.info("Finalize message consume from customer queue: {}", event.getClientId());
    }
}
