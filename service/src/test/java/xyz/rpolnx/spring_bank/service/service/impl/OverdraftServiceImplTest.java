package xyz.rpolnx.spring_bank.service.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import xyz.rpolnx.spring_bank.common.model.dto.AccountEvent;
import xyz.rpolnx.spring_bank.common.model.dto.CustomerEvent;
import xyz.rpolnx.spring_bank.service.external.OverdraftRepository;
import xyz.rpolnx.spring_bank.service.model.entity.Overdraft;
import xyz.rpolnx.spring_bank.service.model.entity.ScoreCategory;
import xyz.rpolnx.spring_bank.service.service.ScoreCategoryService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static xyz.rpolnx.spring_bank.common.model.enums.EventType.*;
import static xyz.rpolnx.spring_bank.common.model.enums.PersonType.PF;

@ExtendWith(MockitoExtension.class)
public class OverdraftServiceImplTest {
    @Mock
    private ScoreCategoryService scoreCategoryService;
    @Mock
    private OverdraftRepository repository;

    @InjectMocks
    private OverdraftServiceImpl service;

    @Test
    public void shouldCreateOverdraft() {
        CustomerEvent.Customer customer = new CustomerEvent.Customer("1", PF, 2, "Test");
        AccountEvent.Account account = new AccountEvent.Account(25L);
        AccountEvent accountEvent = new AccountEvent(CREATION, customer, account);

        ArgumentCaptor<Overdraft> captor = ArgumentCaptor.forClass(Overdraft.class);

        ScoreCategory scoreCategory = new ScoreCategory().withOverdraftLimit(2000d);

        when(scoreCategoryService.getActiveCategory(Mockito.anyInt())).thenReturn(scoreCategory);
        when(repository.save(captor.capture())).thenReturn(null);

        service.createOverdraft(accountEvent);

        assertEquals(captor.getValue().getAccountId(), 25L);
        assertEquals(captor.getValue().getRemainingLimit(), 2000d);
    }

    @Test
    public void shouldIgnoreOverdraftDueToLimit() {
        CustomerEvent.Customer customer = new CustomerEvent.Customer("1", PF, 2, "Test");
        AccountEvent.Account account = new AccountEvent.Account(25L);
        AccountEvent accountEvent = new AccountEvent(CREATION, customer, account);

        ArgumentCaptor<Overdraft> captor = ArgumentCaptor.forClass(Overdraft.class);

        ScoreCategory scoreCategory = new ScoreCategory().withOverdraftLimit(0d);

        when(scoreCategoryService.getActiveCategory(Mockito.anyInt())).thenReturn(scoreCategory);

        service.createOverdraft(accountEvent);

        verify(repository, times(0)).save(any(Overdraft.class));

    }

    @Test
    @DisplayName("When updating for a better category, should increase remaining overdraft limit")
    public void shouldUpdateCreditCardIncreasingLimit() {
        CustomerEvent.Customer customer = new CustomerEvent.Customer("1", PF, 2, "Test");
        AccountEvent.Account account = new AccountEvent.Account(25L);
        AccountEvent accountEvent = new AccountEvent(UPDATE, customer, account);

        ScoreCategory oldCategory = new ScoreCategory().withId(1L).withOverdraftLimit(500d);
        ScoreCategory newCategory = new ScoreCategory().withId(2L).withOverdraftLimit(1000d);

        Overdraft overdraftSpy = spy(new Overdraft().withScoreCategory(oldCategory).withRemainingLimit(500d));
        List<Overdraft> overdrafts = List.of(overdraftSpy);

        when(scoreCategoryService.getActiveCategory(Mockito.anyInt())).thenReturn(newCategory);
        when(repository.findAllByAccountIdAndDeletedOnIsNull(25L)).thenReturn(overdrafts);

        service.updateOverdraft(accountEvent);

        verify(repository, times(0)).save(any(Overdraft.class));

        verify(overdraftSpy).setRemainingLimit(1000d);
    }

    @Test
    @DisplayName("When updating for a worst category, should decrease remaining overdraft limit")
    public void shouldUpdateCreditCardDecreasingLimit() {
        CustomerEvent.Customer customer = new CustomerEvent.Customer("1", PF, 2, "Test");
        AccountEvent.Account account = new AccountEvent.Account(25L);
        AccountEvent accountEvent = new AccountEvent(UPDATE, customer, account);

        ScoreCategory oldCategory = new ScoreCategory().withId(1L).withOverdraftLimit(1000d);
        ScoreCategory newCategory = new ScoreCategory().withId(2L).withOverdraftLimit(500d);

        Overdraft overdraftSpy = spy(new Overdraft().withScoreCategory(oldCategory).withRemainingLimit(600d));
        List<Overdraft> overdrafts = List.of(overdraftSpy);

        when(scoreCategoryService.getActiveCategory(Mockito.anyInt())).thenReturn(newCategory);
        when(repository.findAllByAccountIdAndDeletedOnIsNull(25L)).thenReturn(overdrafts);

        service.updateOverdraft(accountEvent);

        verify(repository, times(0)).save(any(Overdraft.class));

        verify(overdraftSpy).setRemainingLimit(100d);
    }

    @Test
    @DisplayName("When updating for a negative balance, should set current overdraft limit to zero")
    public void shouldUpdateCreditCardSetToZeroLimit() {
        CustomerEvent.Customer customer = new CustomerEvent.Customer("1", PF, 2, "Test");
        AccountEvent.Account account = new AccountEvent.Account(25L);
        AccountEvent accountEvent = new AccountEvent(UPDATE, customer, account);

        ScoreCategory oldCategory = new ScoreCategory().withId(1L).withOverdraftLimit(1000d);
        ScoreCategory newCategory = new ScoreCategory().withId(2L).withOverdraftLimit(300d);

        Overdraft overdraftSpy = spy(new Overdraft().withScoreCategory(oldCategory).withRemainingLimit(200d));
        List<Overdraft> overdrafts = List.of(overdraftSpy);

        when(scoreCategoryService.getActiveCategory(Mockito.anyInt())).thenReturn(newCategory);
        when(repository.findAllByAccountIdAndDeletedOnIsNull(25L)).thenReturn(overdrafts);

        service.updateOverdraft(accountEvent);

        verify(repository, times(0)).save(any(Overdraft.class));

        verify(overdraftSpy).setRemainingLimit(0d);
    }

    @Test
    public void shouldDeleteCreditCard() {
        CustomerEvent.Customer customer = new CustomerEvent.Customer("1", PF, 2, "Test");
        AccountEvent.Account account = new AccountEvent.Account(25L);
        AccountEvent accountEvent = new AccountEvent(DELETE, customer, account);

        ScoreCategory oldCategory = new ScoreCategory().withId(1L).withOverdraftLimit(1000d);

        Overdraft overdraftSpy = spy(new Overdraft().withScoreCategory(oldCategory).withRemainingLimit(200d));
        List<Overdraft> overdrafts = List.of(overdraftSpy);

        when(repository.findAllByAccountIdAndDeletedOnIsNull(25L)).thenReturn(overdrafts);

        service.deleteOverdraft(accountEvent);

        verify(repository, times(0)).save(any(Overdraft.class));

        verify(overdraftSpy).setDeletedOn(any(LocalDateTime.class));
    }
}