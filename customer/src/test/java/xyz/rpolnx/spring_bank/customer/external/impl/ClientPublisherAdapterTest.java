package xyz.rpolnx.spring_bank.customer.external.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import xyz.rpolnx.spring_bank.customer.model.dto.ClientDTO;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static xyz.rpolnx.spring_bank.customer.model.PersonType.PJ;

@ExtendWith(MockitoExtension.class)
public class ClientPublisherAdapterTest {
    @Mock
    private AmqpTemplate amqpTemplate;

    @SuppressWarnings("FieldMayBeFinal")
    @Spy
    private ObjectMapper mapper = new ObjectMapper();

    @InjectMocks
    public ClientPublisherAdapter adapter;

    @SneakyThrows
    @Test
    @DisplayName("When sending message to ampq, should not throw exception")
    public void shouldProcessMessage() {
        ReflectionTestUtils.setField(adapter, "queue", "customer-queue");
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        ClientDTO request = new ClientDTO("123", "Full Name", PJ, 10);

        doNothing().when(amqpTemplate).convertAndSend(anyString(), captor.capture());

        assertDoesNotThrow(() -> adapter.handleClientCreation(request));

        ClientDTO actual = mapper.readValue(captor.getValue(), ClientDTO.class);

        assertEquals(request, actual);
    }

}