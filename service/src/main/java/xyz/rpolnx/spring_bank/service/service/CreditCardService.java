package xyz.rpolnx.spring_bank.service.service;

import xyz.rpolnx.spring_bank.common.model.dto.AccountEvent;

public interface CreditCardService {
    void createCreditCard(AccountEvent event);

    void updateCreditCard(AccountEvent event);

    void deleteCreditCard(AccountEvent event);
}
