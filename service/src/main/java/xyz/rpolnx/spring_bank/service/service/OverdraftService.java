package xyz.rpolnx.spring_bank.service.service;

import xyz.rpolnx.spring_bank.common.model.dto.AccountEvent;
import xyz.rpolnx.spring_bank.common.model.dto.integration.OverdraftDTO;

import java.util.List;

public interface OverdraftService {

    List<OverdraftDTO> getAll();

    List<OverdraftDTO> getByAccountId(Long accountId);

    void createOverdraft(AccountEvent event);

    void updateOverdraft(AccountEvent event);

    void deleteOverdraft(AccountEvent event);
}
