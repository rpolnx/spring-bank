package xyz.rpolnx.spring_bank.customer.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PersonType {
    PF("Pessoa Física", 11), PJ("Pessoa Jurídica", 14);

    private final String displayName;
    private final int documentSize;

    @JsonSerialize
    public String serialize() {
        return this.getDisplayName();
    }
}
