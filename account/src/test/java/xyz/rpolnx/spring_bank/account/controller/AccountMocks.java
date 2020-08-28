package xyz.rpolnx.spring_bank.account.controller;

import xyz.rpolnx.spring_bank.account.model.entity.Account;
import xyz.rpolnx.spring_bank.account.model.enums.AccountStatus;
import xyz.rpolnx.spring_bank.account.model.enums.AccountType;
import xyz.rpolnx.spring_bank.common.model.enums.CustomPersonType;

import java.util.ArrayList;
import java.util.List;

public class AccountMocks {
    public static List<Account> generateAccounts(int size, int max) {
        List<Account> account = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            account.add(generateAccount(i, max));
        }
        return account;
    }

    private static Account generateAccount(int position, int max) {
        int typePositionRandom = (int) (Math.random() * AccountType.values().length);
        AccountType type = AccountType.values()[typePositionRandom];

        int typeAccountStatus = (int) (Math.random() * AccountType.values().length);
        AccountStatus accountStatus = AccountStatus.values()[typeAccountStatus];

        long random = (long) (Math.random() * max);

        return new Account((long) position, String.valueOf(random), "123", type, accountStatus, false);
    }
}
