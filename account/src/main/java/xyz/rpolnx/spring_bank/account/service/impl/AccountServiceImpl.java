package xyz.rpolnx.spring_bank.account.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.rpolnx.spring_bank.account.external.AccountPublisher;
import xyz.rpolnx.spring_bank.account.external.AccountRepository;
import xyz.rpolnx.spring_bank.account.model.dto.AccountEvent;
import xyz.rpolnx.spring_bank.account.model.dto.CustomerEvent;
import xyz.rpolnx.spring_bank.account.model.entity.Account;
import xyz.rpolnx.spring_bank.account.model.enums.AccountStatus;
import xyz.rpolnx.spring_bank.account.model.enums.AccountType;
import xyz.rpolnx.spring_bank.account.service.AccountService;


import static java.util.Arrays.asList;

@Service
@RequiredArgsConstructor
@Log4j2
public class AccountServiceImpl implements AccountService {
    private static final Integer MAX_ACCOUNT_DIGITS = 1000000; // 6 digits for random

    @Value("${agency.number}")
    private String agency;

    private final AccountRepository repository;
    private final AccountPublisher publisher;

    @Override
    @Transactional
    public void createAccount(CustomerEvent event) {
        Long accountNumber = (long) (Math.random() * MAX_ACCOUNT_DIGITS);
        AccountType accountType = AccountType.fromPersonType(event.getPersonType());

        Account account = buildAccount(event.getClientId(), accountNumber, accountType);

        Account saved = repository.save(account);

        AccountEvent accountEvent = new AccountEvent(saved.getId(), event.getClientId(), event.getScore(), event.getType());

        publisher.publishAccountCreationEvent(accountEvent);

        saved.setStatus(AccountStatus.ACTIVE);
    }

    @Override
    @Transactional
    public void updateAccount(CustomerEvent event) {
        Long accountNumber = (long) (Math.random() * MAX_ACCOUNT_DIGITS);
        AccountType accountType = AccountType.fromPersonType(event.getPersonType());

        Account account = repository
                .findFirstByClientIdAndAgencyAndStatusIn(event.getClientId(), agency, asList(AccountStatus.usableStatus()))
                .orElse(buildAccount(event.getClientId(), accountNumber, accountType));

        Account updated = repository.save(account);

        AccountEvent accountEvent = new AccountEvent(updated.getId(), event.getClientId(), event.getScore(), event.getType());

        publisher.publishAccountUpdateEvent(accountEvent);

        updated.setStatus(AccountStatus.ACTIVE);
    }

    @Override
    @Transactional
    public void deleteAccount(CustomerEvent event) {
        repository
                .findFirstByClientIdAndAgencyAndStatusIn(event.getClientId(), agency, asList(AccountStatus.usableStatus()))
                .ifPresentOrElse(it -> it.setStatus(AccountStatus.INACTIVE),
                        () -> log.info("ClientId {}", event.getClientId())
                );
    }

    private Account buildAccount(String clientId, Long accountNumber, AccountType accountType) {
        return Account.builder()
                .number(accountNumber)
                .clientId(clientId)
                .agency(agency)
                .type(accountType)
                .status(AccountStatus.CREATING)
                .build();
    }
}
