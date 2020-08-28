package xyz.rpolnx.spring_bank.common.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import xyz.rpolnx.spring_bank.common.model.enums.EventType;
import xyz.rpolnx.spring_bank.common.model.enums.PersonType;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerEvent extends Event {
    protected Customer customer;

    public CustomerEvent(EventType type, Customer customer) {
        super(type);
        this.customer = customer;
    }

    @AllArgsConstructor
    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Customer {
        private String id;
        private PersonType personType;
        private Integer score;
        private String fullName;
    }
}
