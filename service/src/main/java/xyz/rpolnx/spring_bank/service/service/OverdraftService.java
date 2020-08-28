package xyz.rpolnx.spring_bank.service.service;

import xyz.rpolnx.spring_bank.common.model.dto.AccountEvent;
import xyz.rpolnx.spring_bank.service.model.dto.OverdraftDTO;

import java.util.List;

public interface OverdraftService {

    List<OverdraftDTO> getAll();

    OverdraftDTO getByAccountId(Long accountId);

    void createOverdraft(AccountEvent event);

    void updateOverdraft(AccountEvent event);

    void deleteOverdraft(AccountEvent event);
}
