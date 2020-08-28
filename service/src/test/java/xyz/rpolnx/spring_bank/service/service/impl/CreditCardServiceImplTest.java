package xyz.rpolnx.spring_bank.service.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import xyz.rpolnx.spring_bank.common.model.dto.AccountEvent;
import xyz.rpolnx.spring_bank.common.model.dto.CustomerEvent;
import xyz.rpolnx.spring_bank.service.external.CreditCardRepository;
import xyz.rpolnx.spring_bank.service.model.entity.ScoreCategory;
import xyz.rpolnx.spring_bank.service.service.ScoreCategoryService;

import static org.mockito.Mockito.when;
import static xyz.rpolnx.spring_bank.common.model.enums.EventType.CREATION;
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

        ScoreCategory scoreCategory = new ScoreCategory().withCreditCardLimit(100d);

        when(scoreCategoryService.getActiveCategory(Mockito.anyInt())).thenReturn(scoreCategory);

        creditCardService.createCreditCard(accountEvent);
    }

}