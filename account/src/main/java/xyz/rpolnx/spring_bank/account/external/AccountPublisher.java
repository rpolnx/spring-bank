package xyz.rpolnx.spring_bank.account.external;

import xyz.rpolnx.spring_bank.account.model.dto.AccountEvent;

public interface AccountPublisher {
    void publishAccountCreationEvent(AccountEvent accountEvent);

    void publishAccountUpdateEvent(AccountEvent accountEvent);
}
