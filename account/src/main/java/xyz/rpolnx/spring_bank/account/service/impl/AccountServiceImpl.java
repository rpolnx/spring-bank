package xyz.rpolnx.spring_bank.account.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xyz.rpolnx.spring_bank.account.external.AccountPublisher;
import xyz.rpolnx.spring_bank.account.external.AccountRepository;
import xyz.rpolnx.spring_bank.account.model.entity.Account;
import xyz.rpolnx.spring_bank.account.model.enums.AccountStatus;
import xyz.rpolnx.spring_bank.account.model.enums.AccountType;
import xyz.rpolnx.spring_bank.account.model.dto.CustomerEvent;
import xyz.rpolnx.spring_bank.account.service.AccountService;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private static final Integer MAX_ACCOUNT_DIGITS = 1000000; // 6 digits for random

    @Value("agency.number")
    private String agency;

    private final AccountRepository repository;
    private final AccountPublisher publisher;

    @Override
    @Transactional
    public void createAccount(CustomerEvent service) {
        Long accountNumber = (long) (Math.random() * MAX_ACCOUNT_DIGITS);
        AccountType accountType = AccountType.fromPersonType(service.getPersonType());

        Account account = new Account(accountNumber, service.getClientId(), agency, accountType, AccountStatus.CREATING);

        Account saved = repository.createNewer(account);

        publisher.publishAccountCreationEvent();

        saved.setStatus(AccountStatus.ACTIVE);
    }
}
