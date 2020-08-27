package xyz.rpolnx.spring_bank.account.service;

import xyz.rpolnx.spring_bank.account.model.event.CustomerEvent;

public interface AccountService {
    void createAccount(CustomerEvent service);
}
