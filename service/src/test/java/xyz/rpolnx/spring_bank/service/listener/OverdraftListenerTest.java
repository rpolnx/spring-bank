package xyz.rpolnx.spring_bank.service.listener;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import xyz.rpolnx.spring_bank.common.model.dto.AccountEvent;
import xyz.rpolnx.spring_bank.common.model.dto.CustomerEvent;
import xyz.rpolnx.spring_bank.common.model.enums.EventType;
import xyz.rpolnx.spring_bank.service.service.OverdraftService;

import static org.mockito.Mockito.verify;
import static xyz.rpolnx.spring_bank.common.model.enums.PersonType.PF;

@ExtendWith(MockitoExtension.class)
public class OverdraftListenerTest {
    @Mock
    private OverdraftService service;

    @InjectMocks
    private OverdraftListener listener;

    @Test
    @DisplayName("When receive CREATION event, should handle to createOverdraft method")
    public void handleCreateAccount() {
        CustomerEvent.Customer customer = new CustomerEvent.Customer("123", PF, 10, "Test");
        AccountEvent accountEvent = new AccountEvent(EventType.CREATION, customer, new AccountEvent.Account());

        listener.receive(accountEvent);

        verify(service, Mockito.times(1)).createOverdraft(accountEvent);
    }

    @Test
    @DisplayName("When receive UPDATE event, should handle to updateOverdraft method")
    public void handleUpdateAccount() {
        CustomerEvent.Customer customer = new CustomerEvent.Customer("123", PF, 10, "Test");
        AccountEvent accountEvent = new AccountEvent(EventType.UPDATE, customer, new AccountEvent.Account());

        listener.receive(accountEvent);

        verify(service, Mockito.times(1)).updateOverdraft(accountEvent);
    }

    @Test
    @DisplayName("When receive DELETE event, should handle to deleteOverdraft method")
    public void handleDeleteAccount() {
        CustomerEvent.Customer customer = new CustomerEvent.Customer("123", PF, 10, "Test");
        AccountEvent accountEvent = new AccountEvent(EventType.DELETE, customer, new AccountEvent.Account());

        listener.receive(accountEvent);

        verify(service, Mockito.times(1)).deleteOverdraft(accountEvent);
    }
}