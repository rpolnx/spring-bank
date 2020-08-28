package xyz.rpolnx.spring_bank.service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.rpolnx.spring_bank.service.model.entity.CreditCard;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreditCardDTO {
    private String number;
    private String name;
    private String brandName;
    private String securityCode;
    private LocalDate expiration;
    private Double remainingLimit;

    public static CreditCardDTO fromEntity(CreditCard creditCard) {
        return new CreditCardDTO(creditCard.getNumber(), creditCard.getName(), creditCard.getBrandName(),
                creditCard.getSecurityCode(), creditCard.getExpiration(), creditCard.getRemainingLimit());
    }
}
