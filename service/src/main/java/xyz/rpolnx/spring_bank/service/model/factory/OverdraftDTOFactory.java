package xyz.rpolnx.spring_bank.service.model.factory;

import xyz.rpolnx.spring_bank.common.model.dto.integration.OverdraftDTO;
import xyz.rpolnx.spring_bank.service.model.entity.Overdraft;

public class OverdraftDTOFactory {
    public static OverdraftDTO fromEntity(Overdraft overdraft) {
        return new OverdraftDTO(overdraft.getAccountId(), overdraft.getRemainingLimit());
    }
}
