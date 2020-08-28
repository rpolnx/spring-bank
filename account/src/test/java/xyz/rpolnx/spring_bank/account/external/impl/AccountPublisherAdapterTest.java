package xyz.rpolnx.spring_bank.account.external.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.FanoutExchange;
import xyz.rpolnx.spring_bank.common.model.dto.AccountEvent;
import xyz.rpolnx.spring_bank.common.model.dto.CustomerEvent;
import xyz.rpolnx.spring_bank.common.model.enums.EventType;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AccountPublisherAdapterTest {
    @Mock
    private AmqpTemplate amqpTemplate;
    @Mock
    private FanoutExchange accountExchange;

    @InjectMocks
    private AccountPublisherAdapter publisherAdapter;

    @Test
    @DisplayName("Should send message from amqp template")
    public void shouldSendMessage() {
        AccountEvent accountEvent = new AccountEvent(EventType.CREATION, new CustomerEvent.Customer(), new AccountEvent.Account());

        publisherAdapter.publishAccountEvent(accountEvent);

        verify(amqpTemplate, Mockito.times(1)).convertAndSend(accountExchange.getName(),
                "", accountEvent);
    }


}