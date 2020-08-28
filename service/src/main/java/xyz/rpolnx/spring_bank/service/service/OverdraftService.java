package xyz.rpolnx.spring_bank.service.service;

import xyz.rpolnx.spring_bank.common.model.dto.AccountEvent;

public interface OverdraftService {
    void createOverdraft(AccountEvent event);

    void updateOverdraft(AccountEvent event);

    void deleteOverdraft(AccountEvent event);
}
