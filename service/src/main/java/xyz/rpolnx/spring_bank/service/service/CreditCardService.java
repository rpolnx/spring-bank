package xyz.rpolnx.spring_bank.service.service;

import xyz.rpolnx.spring_bank.service.model.dto.AccountEvent;

public interface CreditCardService {
    void createCreditCard(AccountEvent accountEvent);

    void updateCreditCard(AccountEvent accountEvent);

    void deleteCreditCard(AccountEvent accountEvent);
}
