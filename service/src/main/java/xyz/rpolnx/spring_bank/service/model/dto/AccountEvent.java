package xyz.rpolnx.spring_bank.service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.rpolnx.spring_bank.common.model.enums.EventType;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountEvent {
    private Long accountId;
    private String clientId;
    private Integer score;
    private EventType type;
}