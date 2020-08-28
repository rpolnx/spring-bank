package xyz.rpolnx.spring_bank.account.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import xyz.rpolnx.spring_bank.account.external.AccountPublisher;
import xyz.rpolnx.spring_bank.account.external.AccountRepository;
import xyz.rpolnx.spring_bank.account.model.entity.Account;
import xyz.rpolnx.spring_bank.account.model.enums.AccountStatus;
import xyz.rpolnx.spring_bank.account.model.enums.AccountType;
import xyz.rpolnx.spring_bank.common.model.dto.AccountEvent;
import xyz.rpolnx.spring_bank.common.model.dto.CustomerEvent;

import java.util.Optional;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static xyz.rpolnx.spring_bank.common.model.enums.EventType.*;
import static xyz.rpolnx.spring_bank.common.model.enums.EventType.DELETE;
import static xyz.rpolnx.spring_bank.common.model.enums.PersonType.PF;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {
    @Mock
    private AccountRepository repository;
    @Mock
    private AccountPublisher publisher;
    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(accountService, "agency", "1234");
    }

    @Test
    @DisplayName("When receive creation event, should create account")
    public void shouldCreateAccount() {
        Account account = new Account(12L, "id", "1234", AccountType.C, AccountStatus.CREATING, true);

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        when(repository.save(captor.capture())).thenReturn(account);

        CustomerEvent.Customer customer = new CustomerEvent.Customer("id", PF, 10, "test");
        CustomerEvent customerEvent = new CustomerEvent(CREATION, customer);

        AccountEvent.Account account1 = new AccountEvent.Account(12L);
        AccountEvent accountEvent = new AccountEvent(CREATION, customerEvent.getCustomer(), account1);

        accountService.createAccount(customerEvent);

        assertNotNull(captor.getValue().getNumber());
        assertEquals("id", captor.getValue().getClientId());
        assertEquals("1234", captor.getValue().getAgency());

        verify(publisher, Mockito.times(1)).publishAccountEvent(accountEvent);
    }

    @Test
    @DisplayName("When receive update event, should update account")
    public void shouldUpdateAccount() {
        Account account = new Account(12L, "id", "1234", AccountType.C, AccountStatus.CREATING, true);

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        when(repository.findFirstByClientIdAndAgencyAndStatusIn("id", "1234", asList(AccountStatus.usableStatus())))
                .thenReturn(Optional.of(account));

        when(repository.save(captor.capture())).thenReturn(account);

        CustomerEvent.Customer customer = new CustomerEvent.Customer("id", PF, 10, "test");
        CustomerEvent customerEvent = new CustomerEvent(UPDATE, customer);

        AccountEvent.Account account1 = new AccountEvent.Account(12L);
        AccountEvent accountEvent = new AccountEvent(UPDATE, customerEvent.getCustomer(), account1);

        accountService.updateAccount(customerEvent);

        assertNotNull(captor.getValue().getNumber());
        assertEquals("id", captor.getValue().getClientId());
        assertEquals("1234", captor.getValue().getAgency());

        verify(publisher, Mockito.times(1)).publishAccountEvent(accountEvent);
    }

    @Test
    @DisplayName("When receive update event, should create a non existent account")
    public void shouldCreateAccountByUpdateEvent() {
        Account account = new Account(12L, "id", "1234", AccountType.C, AccountStatus.CREATING, true);

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        when(repository.findFirstByClientIdAndAgencyAndStatusIn("id", "1234", asList(AccountStatus.usableStatus())))
                .thenReturn(Optional.empty());

        when(repository.save(captor.capture())).thenReturn(account);

        CustomerEvent.Customer customer = new CustomerEvent.Customer("id", PF, 10, "test");
        CustomerEvent customerEvent = new CustomerEvent(UPDATE, customer);

        AccountEvent.Account account1 = new AccountEvent.Account(12L);
        AccountEvent accountEvent = new AccountEvent(UPDATE, customerEvent.getCustomer(), account1);

        accountService.updateAccount(customerEvent);

        assertNotNull(captor.getValue().getNumber());
        assertEquals("id", captor.getValue().getClientId());
        assertEquals("1234", captor.getValue().getAgency());

        verify(publisher, Mockito.times(1)).publishAccountEvent(accountEvent);
    }

    @Test
    @DisplayName("When receive delete event, should delete account")
    public void shouldDeleteAccount() {
        Account account = new Account(12L, "id", "1234", AccountType.C, AccountStatus.CREATING, true);

        when(repository.findFirstByClientIdAndAgencyAndStatusIn("id", "1234", asList(AccountStatus.usableStatus())))
                .thenReturn(Optional.of(account));

        CustomerEvent.Customer customer = new CustomerEvent.Customer("id", PF, 10, "test");
        CustomerEvent customerEvent = new CustomerEvent(DELETE, customer);

        AccountEvent.Account account1 = new AccountEvent.Account(12L);
        AccountEvent accountEvent = new AccountEvent(DELETE, customerEvent.getCustomer(), account1);

        accountService.deleteAccount(customerEvent);

        verify(publisher, Mockito.times(1)).publishAccountEvent(accountEvent);
    }

    @Test
    @DisplayName("When receive delete event and account doesn't exist, should log and finalize handling event")
    public void shouldDoNothingOnDelete() {
        when(repository.findFirstByClientIdAndAgencyAndStatusIn("id", "1234", asList(AccountStatus.usableStatus())))
                .thenReturn(Optional.empty());

        CustomerEvent.Customer customer = new CustomerEvent.Customer("id", PF, 10, "test");
        CustomerEvent customerEvent = new CustomerEvent(DELETE, customer);

        AccountEvent.Account account1 = new AccountEvent.Account(12L);
        AccountEvent accountEvent = new AccountEvent(DELETE, customerEvent.getCustomer(), account1);

        accountService.deleteAccount(customerEvent);
    }

}