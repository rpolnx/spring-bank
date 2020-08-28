package xyz.rpolnx.spring_bank.service.model.factory;

import xyz.rpolnx.spring_bank.common.model.dto.AccountEvent;
import xyz.rpolnx.spring_bank.common.model.dto.CustomerEvent;
import xyz.rpolnx.spring_bank.service.model.entity.CreditCard;
import xyz.rpolnx.spring_bank.service.model.entity.ScoreCategory;

import java.time.LocalDate;

public class CreditCardBuilder {
    private CustomerEvent.Customer customer = new CustomerEvent.Customer();
    private AccountEvent.Account account = new AccountEvent.Account();
    private ScoreCategory scoreCategory = new ScoreCategory();

    private String cardNumber;
    private String brandName;
    private String securityCode;
    private LocalDate expiration;

    public static CreditCardBuilder builder() {
        return new CreditCardBuilder();
    }

    public CreditCardBuilder withCustomerInfo(CustomerEvent.Customer customer) {
        this.customer = customer;
        return this;
    }

    public CreditCardBuilder withAccountInfo(AccountEvent.Account account) {
        this.account = account;
        return this;
    }

    public CreditCardBuilder withScoreCategory(ScoreCategory scoreCategory) {
        this.scoreCategory = scoreCategory;
        return this;
    }

    public CreditCardBuilder withCardInfo(String cardNumber, String brandName, String securityCode, LocalDate expiration) {
        this.cardNumber = cardNumber;
        this.brandName = brandName;
        this.securityCode = securityCode;
        this.expiration = expiration;
        return this;
    }

    public CreditCard build() {
        return new CreditCard(cardNumber, account.getId(), customer.getFullName(), brandName, securityCode, expiration,
                scoreCategory.getCreditCardLimit(), scoreCategory, null);
    }
}
