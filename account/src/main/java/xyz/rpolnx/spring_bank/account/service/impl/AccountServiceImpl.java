package xyz.rpolnx.spring_bank.account.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.rpolnx.spring_bank.account.external.AccountPublisher;
import xyz.rpolnx.spring_bank.account.external.AccountRepository;
import xyz.rpolnx.spring_bank.account.model.dto.AccountDTO;
import xyz.rpolnx.spring_bank.account.model.entity.Account;
import xyz.rpolnx.spring_bank.account.model.enums.AccountStatus;
import xyz.rpolnx.spring_bank.account.model.enums.AccountType;
import xyz.rpolnx.spring_bank.account.model.factory.AccountEventFactory;
import xyz.rpolnx.spring_bank.account.service.AccountService;
import xyz.rpolnx.spring_bank.common.model.dto.AccountEvent;
import xyz.rpolnx.spring_bank.common.model.dto.CustomerEvent;

import java.util.List;

import static java.util.Arrays.asList;
import static xyz.rpolnx.spring_bank.account.model.factory.AccountFactory.generateAccount;

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
        AccountType accountType = AccountType.fromPersonType(event.getCustomer().getPersonType());
        Account account = generateAccount(event.getCustomer().getId(), accountNumber, agency, accountType);

        Account saved = repository.save(account);

        AccountEvent accountEvent = AccountEventFactory.generateEvent(saved, event);

        publisher.publishAccountCreationEvent(accountEvent);

        saved.setStatus(AccountStatus.ACTIVE);
    }

    @Override
    @Transactional
    public void updateAccount(CustomerEvent event) {
        Long accountNumber = (long) (Math.random() * MAX_ACCOUNT_DIGITS);
        CustomerEvent.Customer customer = event.getCustomer();
        AccountType accountType = AccountType.fromPersonType(customer.getPersonType());

        Account account = repository
                .findFirstByClientIdAndAgencyAndStatusIn(customer.getId(), agency, asList(AccountStatus.usableStatus()))
                .orElse(generateAccount(customer.getId(), accountNumber, agency, accountType));

        Account updated = repository.save(account);

        AccountEvent accountEvent = AccountEventFactory.generateEvent(updated, event);

        publisher.publishAccountUpdateEvent(accountEvent);

        updated.setStatus(AccountStatus.ACTIVE);
    }

    @Override
    @Transactional
    public void deleteAccount(CustomerEvent event) {
        repository
                .findFirstByClientIdAndAgencyAndStatusIn(event.getCustomer().getId(), agency, asList(AccountStatus.usableStatus()))
                .ifPresentOrElse(it -> {
                            it.setStatus(AccountStatus.INACTIVE);
                            AccountEvent accountEvent = AccountEventFactory.generateEvent(it, event);
                            publisher.publishAccountUpdateEvent(accountEvent);
                        },
                        () -> log.info("ClientId {}", event.getCustomer().getId())
                );
    }

    @Override
    public List<AccountDTO> getAll() {
        return null;
    }

    @Override
    public AccountDTO getByAccountId(Long accountId) {
        return null;
    }
}
