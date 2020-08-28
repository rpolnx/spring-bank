package xyz.rpolnx.spring_bank.service.service.impl;

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
import static xyz.rpolnx.spring_bank.common.model.enums.EventType.CREATION;
import static xyz.rpolnx.spring_bank.common.model.enums.EventType.DELETE;
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
    public void shouldDeleteCreditCard() {
        CustomerEvent.Customer customer = new CustomerEvent.Customer("1", PF, 2, "Test");
        AccountEvent.Account account = new AccountEvent.Account(25L);
        AccountEvent accountEvent = new AccountEvent(DELETE, customer, account);

        ScoreCategory scoreCategory = new ScoreCategory().withCreditCardLimit(0d);

        CreditCard creditCardSpy = spy(new CreditCard());
        List<CreditCard> creditCards = List.of(creditCardSpy);

        when(repository.findAllByAccountIdAndDeletedOnIsNull(25L)).thenReturn(creditCards);

        creditCardService.deleteCreditCard(accountEvent);

        verify(repository, times(0)).save(any(CreditCard.class));

        verify(creditCardSpy).setDeletedOn(any(LocalDateTime.class));
    }

}