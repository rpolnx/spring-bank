package xyz.rpolnx.spring_bank.common.model.dto.integration;

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
}
