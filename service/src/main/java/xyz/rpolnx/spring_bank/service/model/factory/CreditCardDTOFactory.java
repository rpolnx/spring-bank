package xyz.rpolnx.spring_bank.service.model.factory;

import xyz.rpolnx.spring_bank.common.model.dto.integration.CreditCardDTO;
import xyz.rpolnx.spring_bank.service.model.entity.CreditCard;

public class CreditCardDTOFactory {
    public static CreditCardDTO fromEntity(CreditCard creditCard) {
        return new CreditCardDTO(creditCard.getNumber(), creditCard.getName(), creditCard.getBrandName(),
                creditCard.getSecurityCode(), creditCard.getExpiration(), creditCard.getRemainingLimit());
    }
}
