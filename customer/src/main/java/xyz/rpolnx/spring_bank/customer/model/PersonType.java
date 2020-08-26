package xyz.rpolnx.spring_bank.customer.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PersonType {
    PF("Pessoa Física"), PJ("Pessoa Jurídica");

    private final String displayName;

    @JsonSerialize
    public String serialize() {
        return this.getDisplayName();
    }
}
