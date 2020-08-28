package xyz.rpolnx.spring_bank.account.service;

import xyz.rpolnx.spring_bank.account.model.dto.AccountDTO;
import xyz.rpolnx.spring_bank.common.model.dto.CustomerEvent;

import java.util.List;

public interface AccountService {
    void createAccount(CustomerEvent event);

    void updateAccount(CustomerEvent event);

    void deleteAccount(CustomerEvent event);

    List<AccountDTO> getAll();

    AccountDTO getByAccountId(Long accountId);
}
