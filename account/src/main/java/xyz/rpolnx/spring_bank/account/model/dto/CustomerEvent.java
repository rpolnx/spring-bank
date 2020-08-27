package xyz.rpolnx.spring_bank.account.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.rpolnx.spring_bank.account.model.enums.PersonType;
import xyz.rpolnx.spring_bank.common.model.enums.EventType;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerEvent {
    private String clientId;
    private PersonType personType;
    private Integer score;
    private EventType type;
}
