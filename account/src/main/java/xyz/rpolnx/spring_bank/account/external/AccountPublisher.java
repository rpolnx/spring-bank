package xyz.rpolnx.spring_bank.account.external;

import xyz.rpolnx.spring_bank.common.model.dto.AccountEvent;

public interface AccountPublisher {
    void publishAccountEvent(AccountEvent accountEvent);
}
