package xyz.rpolnx.spring_bank.service.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import xyz.rpolnx.spring_bank.common.model.dto.AccountEvent;
import xyz.rpolnx.spring_bank.service.model.enums.CreditCardEventHandler;
import xyz.rpolnx.spring_bank.service.service.CreditCardService;

@Component
@RequiredArgsConstructor
@Log4j2
public class CreditCardListener {
    private final CreditCardService service;

    @RabbitListener(queues = "${credit-card-queue-name}")
    public void receive(@Payload final AccountEvent event) {
        log.info("Consuming message from credit card queue: {}", event);

        CreditCardEventHandler eventHandler = CreditCardEventHandler.fromEventType(event.getType());

        eventHandler.getCallable().accept(service, event);

        log.info("Finalize message consume from credit card queue with accountId: {}", event.getAccount().getId());
    }


}
