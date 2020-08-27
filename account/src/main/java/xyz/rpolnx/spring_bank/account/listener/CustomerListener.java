package xyz.rpolnx.spring_bank.account.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import xyz.rpolnx.spring_bank.account.model.event.CustomerEvent;
import xyz.rpolnx.spring_bank.account.service.AccountService;

@Component
@RequiredArgsConstructor
public class CustomerListener {
    private AccountService service;
    private ObjectMapper mapper;

    @RabbitListener(queues = "${customer-queue-name}")
    public void receive(final CustomerEvent event) {
        service.createAccount(event);
    }
}
