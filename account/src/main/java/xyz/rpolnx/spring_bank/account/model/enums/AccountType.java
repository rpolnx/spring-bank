package xyz.rpolnx.spring_bank.account.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;

import static java.util.stream.Collectors.toMap;
import static xyz.rpolnx.spring_bank.account.model.enums.PersonType.PF;
import static xyz.rpolnx.spring_bank.account.model.enums.PersonType.PJ;

@AllArgsConstructor
@Getter
public enum AccountType {
    C("Conta Corrente", PF),
    E("Conta Empresarial", PJ);

    private String type;
    private PersonType personType;
    private static final Map<PersonType, AccountType> map;

    static {
        map = Arrays.stream(AccountType.values())
                .collect(toMap(AccountType::getPersonType, x -> x));
    }

    public static AccountType fromPersonType(PersonType personType) {
        return map.get(personType);
    }
}
