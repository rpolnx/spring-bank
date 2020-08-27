package xyz.rpolnx.spring_bank.account.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.rpolnx.spring_bank.common.model.enums.EventType;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountEvent {
    @JsonProperty("accountId")
    private Long accountNumber;
    private String clientId;
    private Integer score;
    private EventType type;
}
