package xyz.rpolnx.spring_bank.account.service;

import xyz.rpolnx.spring_bank.account.model.dto.CustomerEvent;

public interface AccountService {
    void createAccount(CustomerEvent service);

    void updateAccount(CustomerEvent service);

    void deleteAccount(CustomerEvent customerEvent);
}
