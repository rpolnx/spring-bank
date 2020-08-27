package xyz.rpolnx.spring_bank.customer.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import xyz.rpolnx.spring_bank.customer.exceptions.BadRequestException;

@Getter
@AllArgsConstructor
public enum PersonType {
    PF("Pessoa Física", 11), PJ("Pessoa Jurídica", 14);

    private final String displayName;
    private final int documentSize;

    public void verifyDocumentSize(String documentNumber) {
        if (documentNumber.length() != this.getDocumentSize()) {
            throw new BadRequestException(this + " must has document size of " + this.getDocumentSize());
        }
    }

    @JsonSerialize
    public String serialize() {
        return this.getDisplayName();
    }
}
