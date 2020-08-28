package xyz.rpolnx.spring_bank.common.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import xyz.rpolnx.spring_bank.common.model.enums.PersonType;

@Getter
@AllArgsConstructor
public enum CustomPersonType {
    PF("Pessoa Física", 11), PJ("Pessoa Jurídica", 14);

    private final String displayName;
    private final int documentSize;

    public PersonType toCommonPersonType() {
        return PersonType.valueOf(this.name());
    }
}
