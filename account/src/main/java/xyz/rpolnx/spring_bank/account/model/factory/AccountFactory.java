package xyz.rpolnx.spring_bank.account.model.factory;

import xyz.rpolnx.spring_bank.account.model.entity.Account;
import xyz.rpolnx.spring_bank.account.model.enums.AccountType;

import static xyz.rpolnx.spring_bank.account.model.enums.AccountStatus.CREATING;

public class AccountFactory {
    public static Account generateAccount(String clientId, Long accountNumber, String agency, AccountType accountType) {
        return Account.builder()
                .number(accountNumber)
                .clientId(clientId)
                .agency(agency)
                .type(accountType)
                .status(CREATING)
                .build();
    }
}
