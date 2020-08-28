package xyz.rpolnx.spring_bank.account.model.factory;


import xyz.rpolnx.spring_bank.account.model.dto.AccountDTO;
import xyz.rpolnx.spring_bank.account.model.entity.Account;

public class AccountDTOFactory {
    public static AccountDTO fromEntity(Account account) {
        AccountDTO.Account account1 = new AccountDTO.Account(account.getNumber(), account.getClientId(),
                account.getAgency(), account.getType(), account.getStatus());
        return new AccountDTO(account1);
    }
}
