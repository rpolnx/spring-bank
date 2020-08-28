package xyz.rpolnx.spring_bank.service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.rpolnx.spring_bank.service.model.entity.Overdraft;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OverdraftDTO {
    private Double remainingLimit;

    public static OverdraftDTO fromEntity(Overdraft overdraft) {
        return new OverdraftDTO(overdraft.getRemainingLimit());
    }
}
