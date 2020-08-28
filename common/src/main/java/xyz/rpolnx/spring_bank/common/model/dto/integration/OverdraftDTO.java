package xyz.rpolnx.spring_bank.common.model.dto.integration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OverdraftDTO {
    private Double remainingLimit;
}
