package xyz.rpolnx.spring_bank.service.service;

import xyz.rpolnx.spring_bank.common.model.dto.AccountEvent;
import xyz.rpolnx.spring_bank.common.model.dto.integration.CreditCardDTO;

import java.util.List;

public interface CreditCardService {

    List<CreditCardDTO> getAll();

    List<CreditCardDTO> getByAccountId(Long accountId);

    void createCreditCard(AccountEvent event);

    void updateCreditCard(AccountEvent event);

    void deleteCreditCard(AccountEvent event);
}
