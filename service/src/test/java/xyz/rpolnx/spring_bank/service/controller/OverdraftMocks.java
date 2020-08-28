package xyz.rpolnx.spring_bank.service.controller;

import xyz.rpolnx.spring_bank.service.model.entity.Overdraft;

import java.util.ArrayList;
import java.util.List;

public class OverdraftMocks {
    public static List<Overdraft> generateOverdrafts(int size) {
        List<Overdraft> account = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            account.add(generateOverdraft(i));
        }
        return account;
    }

    private static Overdraft generateOverdraft(int position) {
        double random = (Math.random() * 2000);

        return new Overdraft((long) position, random, null, null);
    }
}
