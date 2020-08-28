package xyz.rpolnx.spring_bank.account.model.factory;

import xyz.rpolnx.spring_bank.common.model.dto.AccountEvent;
import xyz.rpolnx.spring_bank.account.model.entity.Account;
import xyz.rpolnx.spring_bank.common.model.dto.CustomerEvent;

public class AccountEventFactory {
    public static AccountEvent generateEvent(Account entity, CustomerEvent event) {
        AccountEvent.Account account = new AccountEvent.Account(entity.getId());
        return new AccountEvent(event.getType(), event.getCustomer(), account);
    }
}
