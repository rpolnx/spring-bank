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
import xyz.rpolnx.spring_bank.service.external.CreditCardRepository;
import xyz.rpolnx.spring_bank.service.model.entity.CreditCard;
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
public class CreditCardServiceImplTest {
    @Mock
    private ScoreCategoryService scoreCategoryService;
    @Mock
    private CreditCardRepository repository;

    @InjectMocks
    private CreditCardServiceImpl creditCardService;

    @Test
    public void shouldCreateCreditCard() {
        CustomerEvent.Customer customer = new CustomerEvent.Customer("1", PF, 2, "Test");
        AccountEvent.Account account = new AccountEvent.Account(25L);
        AccountEvent accountEvent = new AccountEvent(CREATION, customer, account);

        ArgumentCaptor<CreditCard> captor = ArgumentCaptor.forClass(CreditCard.class);

        ScoreCategory scoreCategory = new ScoreCategory().withCreditCardLimit(100d);

        when(scoreCategoryService.getActiveCategory(Mockito.anyInt())).thenReturn(scoreCategory);
        when(repository.save(captor.capture())).thenReturn(null);

        creditCardService.createCreditCard(accountEvent);

        assertEquals(captor.getValue().getBrandName(), "My brand");
        assertEquals(captor.getValue().getNumber().charAt(0), '9');
        assertEquals(captor.getValue().getNumber().length(), 16);
    }

    @Test
    public void shouldNotCreateCreditCard() {
        CustomerEvent.Customer customer = new CustomerEvent.Customer("1", PF, 2, "Test");
        AccountEvent.Account account = new AccountEvent.Account(25L);
        AccountEvent accountEvent = new AccountEvent(CREATION, customer, account);

        ScoreCategory scoreCategory = new ScoreCategory().withCreditCardLimit(0d);

        when(scoreCategoryService.getActiveCategory(Mockito.anyInt())).thenReturn(scoreCategory);

        creditCardService.createCreditCard(accountEvent);

        verify(repository, times(0)).save(any(CreditCard.class));
    }

    @Test
    @DisplayName("When updating for a better category, should increase remaining limit")
    public void shouldUpdateCreditCardIncreasingLimit() {
        CustomerEvent.Customer customer = new CustomerEvent.Customer("1", PF, 2, "Test");
        AccountEvent.Account account = new AccountEvent.Account(25L);
        AccountEvent accountEvent = new AccountEvent(UPDATE, customer, account);

        ScoreCategory oldCategory = new ScoreCategory().withId(1L).withCreditCardLimit(500d);
        ScoreCategory newCategory = new ScoreCategory().withId(2L).withCreditCardLimit(1000d);

        CreditCard creditCardSpy = spy(new CreditCard().withScoreCategory(oldCategory).withRemainingLimit(500d));
        List<CreditCard> creditCards = List.of(creditCardSpy);

        when(scoreCategoryService.getActiveCategory(Mockito.anyInt())).thenReturn(newCategory);
        when(repository.findAllByAccountIdAndDeletedOnIsNull(25L)).thenReturn(creditCards);

        creditCardService.updateCreditCard(accountEvent);

        verify(repository, times(0)).save(any(CreditCard.class));

        verify(creditCardSpy).setRemainingLimit(1000d);
    }

    @Test
    @DisplayName("When updating for a worst category, should decrease remaining limit")
    public void shouldUpdateCreditCardDecreasingLimit() {
        CustomerEvent.Customer customer = new CustomerEvent.Customer("1", PF, 2, "Test");
        AccountEvent.Account account = new AccountEvent.Account(25L);
        AccountEvent accountEvent = new AccountEvent(UPDATE, customer, account);

        ScoreCategory oldCategory = new ScoreCategory().withId(1L).withCreditCardLimit(1000d);
        ScoreCategory newCategory = new ScoreCategory().withId(2L).withCreditCardLimit(500d);

        CreditCard creditCardSpy = spy(new CreditCard().withScoreCategory(oldCategory).withRemainingLimit(600d));
        List<CreditCard> creditCards = List.of(creditCardSpy);

        when(scoreCategoryService.getActiveCategory(Mockito.anyInt())).thenReturn(newCategory);
        when(repository.findAllByAccountIdAndDeletedOnIsNull(25L)).thenReturn(creditCards);

        creditCardService.updateCreditCard(accountEvent);

        verify(repository, times(0)).save(any(CreditCard.class));

        verify(creditCardSpy).setRemainingLimit(100d);
    }

    @Test
    @DisplayName("When updating for a negative balance, should set current limit to zero")
    public void shouldUpdateCreditCardSetToZeroLimit() {
        CustomerEvent.Customer customer = new CustomerEvent.Customer("1", PF, 2, "Test");
        AccountEvent.Account account = new AccountEvent.Account(25L);
        AccountEvent accountEvent = new AccountEvent(UPDATE, customer, account);

        ScoreCategory oldCategory = new ScoreCategory().withId(1L).withCreditCardLimit(1000d);
        ScoreCategory newCategory = new ScoreCategory().withId(2L).withCreditCardLimit(500d);

        CreditCard creditCardSpy = spy(new CreditCard().withScoreCategory(oldCategory).withRemainingLimit(200d));
        List<CreditCard> creditCards = List.of(creditCardSpy);

        when(scoreCategoryService.getActiveCategory(Mockito.anyInt())).thenReturn(newCategory);
        when(repository.findAllByAccountIdAndDeletedOnIsNull(25L)).thenReturn(creditCards);

        creditCardService.updateCreditCard(accountEvent);

        verify(repository, times(0)).save(any(CreditCard.class));

        verify(creditCardSpy).setRemainingLimit(0d);
    }

    @Test
    public void shouldDeleteCreditCard() {
        CustomerEvent.Customer customer = new CustomerEvent.Customer("1", PF, 2, "Test");
        AccountEvent.Account account = new AccountEvent.Account(25L);
        AccountEvent accountEvent = new AccountEvent(DELETE, customer, account);

        CreditCard creditCardSpy = spy(new CreditCard());
        List<CreditCard> creditCards = List.of(creditCardSpy);

        when(repository.findAllByAccountIdAndDeletedOnIsNull(25L)).thenReturn(creditCards);

        creditCardService.deleteCreditCard(accountEvent);

        verify(repository, times(0)).save(any(CreditCard.class));

        verify(creditCardSpy).setDeletedOn(any(LocalDateTime.class));
    }

}