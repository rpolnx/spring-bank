package xyz.rpolnx.spring_bank.account.external.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.stereotype.Component;
import xyz.rpolnx.spring_bank.account.external.AccountPublisher;
import xyz.rpolnx.spring_bank.common.model.dto.AccountEvent;

@Component
@RequiredArgsConstructor
@Log4j2
public class AccountPublisherAdapter implements AccountPublisher {
    private final AmqpTemplate amqpTemplate;
    private final FanoutExchange accountExchange;

    @Override
    public void publishAccountEvent(AccountEvent accountEvent) {
        log.info("Publishing message: {}", accountEvent);

        amqpTemplate.convertAndSend(accountExchange.getName(), "", accountEvent);

        log.info("Finalizing message publisher from accountEvent: {}", accountEvent.getAccount().getId());
    }
}
