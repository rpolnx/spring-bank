package xyz.rpolnx.spring_bank.service.controller;

import xyz.rpolnx.spring_bank.service.model.entity.CreditCard;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CreditCardMocks {
    public static List<CreditCard> generateCreditCards(int size, int max) {
        List<CreditCard> account = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            account.add(generateCreditCard(i, max));
        }
        return account;
    }

    private static CreditCard generateCreditCard(int position, int max) {
        long random = (long) (Math.random() * max);

        return new CreditCard(String.valueOf(random), (long) position, "name", "any", "fix",
                LocalDate.now(), 20d, null, null);
    }
}
