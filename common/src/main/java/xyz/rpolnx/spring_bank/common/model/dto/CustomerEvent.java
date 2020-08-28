package xyz.rpolnx.spring_bank.common.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.rpolnx.spring_bank.common.model.enums.EventType;
import xyz.rpolnx.spring_bank.common.model.enums.PersonType;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerEvent {
    private Customer customer;
    private EventType type;

    @AllArgsConstructor
    @Data
    @NoArgsConstructor
    public static class Customer {
        private String clientId;
        private PersonType personType;
        private Integer score;
        private String fullName;
    }
}
