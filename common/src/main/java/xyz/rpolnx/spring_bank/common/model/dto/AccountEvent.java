package xyz.rpolnx.spring_bank.common.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import xyz.rpolnx.spring_bank.common.model.dto.CustomerEvent;
import xyz.rpolnx.spring_bank.common.model.enums.EventType;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class AccountEvent extends CustomerEvent {
    private Account account;

    public AccountEvent(EventType type, Customer customer, Account account) {
        super(type, customer);
        this.account = account;
    }

    @AllArgsConstructor
    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Account {
        private Long id;
    }
}
