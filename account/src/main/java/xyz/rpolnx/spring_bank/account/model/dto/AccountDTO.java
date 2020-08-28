package xyz.rpolnx.spring_bank.account.model.dto;

import lombok.*;
import xyz.rpolnx.spring_bank.account.model.enums.AccountStatus;
import xyz.rpolnx.spring_bank.account.model.enums.AccountType;
import xyz.rpolnx.spring_bank.common.model.dto.integration.CreditCardDTO;
import xyz.rpolnx.spring_bank.common.model.dto.integration.OverdraftDTO;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
@With
@Builder
public class AccountDTO {
    private Account account;
    private List<CreditCardDTO> cards;
    private List<OverdraftDTO> overdrafts;

    public AccountDTO(Account account) {
        this.account = account;
    }

    @AllArgsConstructor
    @Data
    @NoArgsConstructor
    @Builder
    public static class Account {
        private Long number;
        private String clientId;
        private String agency;
        private AccountType type;
        private AccountStatus status;
    }
}
