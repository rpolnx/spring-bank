package xyz.rpolnx.spring_bank.account.listener;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import xyz.rpolnx.spring_bank.account.service.AccountService;
import xyz.rpolnx.spring_bank.common.model.dto.CustomerEvent;
import xyz.rpolnx.spring_bank.common.model.enums.EventType;

import static org.mockito.Mockito.verify;
import static xyz.rpolnx.spring_bank.common.model.enums.PersonType.PF;

@ExtendWith(MockitoExtension.class)
public class CustomerListenerTest {
    @Mock
    private AccountService service;

    @InjectMocks
    private CustomerListener listener;

    @Test
    @DisplayName("When receive CREATION event, should handle to createAccount method")
    public void handleCreateAccount() {
        CustomerEvent.Customer customer = new CustomerEvent.Customer("123", PF, 10, "Test");
        CustomerEvent customerEvent = new CustomerEvent(EventType.CREATION, customer);

        listener.receive(customerEvent);

        verify(service, Mockito.times(1)).createAccount(customerEvent);
    }

    @Test
    @DisplayName("When receive CREATION event, should handle to createAccount method")
    public void handleUpdateAccount() {
        CustomerEvent.Customer customer = new CustomerEvent.Customer("123", PF, 10, "Test");
        CustomerEvent customerEvent = new CustomerEvent(EventType.UPDATE, customer);

        listener.receive(customerEvent);

        verify(service, Mockito.times(1)).updateAccount(customerEvent);
    }

    @Test
    @DisplayName("When receive CREATION event, should handle to createAccount method")
    public void handleDeleteAccount() {
        CustomerEvent.Customer customer = new CustomerEvent.Customer("123", PF, 10, "Test");
        CustomerEvent customerEvent = new CustomerEvent(EventType.DELETE, customer);

        listener.receive(customerEvent);

        verify(service, Mockito.times(1)).deleteAccount(customerEvent);
    }

    @Test
    @DisplayName("When receive unknown event, should log and do nothing")
    public void handleUnknownError() {
        CustomerEvent.Customer customer = new CustomerEvent.Customer("123", PF, 10, "Test");
        CustomerEvent customerEvent = new CustomerEvent(null, customer);

        listener.receive(customerEvent);
    }

}