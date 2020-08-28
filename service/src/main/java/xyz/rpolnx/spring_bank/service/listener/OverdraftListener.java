package xyz.rpolnx.spring_bank.service.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import xyz.rpolnx.spring_bank.common.model.dto.AccountEvent;
import xyz.rpolnx.spring_bank.service.model.enums.OverdraftEventHandler;
import xyz.rpolnx.spring_bank.service.service.OverdraftService;

@Component
@RequiredArgsConstructor
@Log4j2
public class OverdraftListener {
    private final OverdraftService service;

    @RabbitListener(queues = "${overdraft-queue-name}")
    public void receive(@Payload final AccountEvent event) {
        log.info("Consuming message from overdraft queue: {}", event);

        OverdraftEventHandler eventHandler = OverdraftEventHandler.fromEventType(event.getType());

        eventHandler.getCallable().accept(service, event);

        log.info("Finalize message consume from overdraft queue with accountId: {}", event.getAccount().getId());
    }


}
