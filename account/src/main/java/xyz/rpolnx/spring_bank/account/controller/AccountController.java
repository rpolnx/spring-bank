package xyz.rpolnx.spring_bank.account.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.rpolnx.spring_bank.account.model.dto.AccountDTO;
import xyz.rpolnx.spring_bank.account.service.AccountService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("accounts")
public class AccountController {
    private final AccountService service;

    @GetMapping
    public List<AccountDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("{accountId}")
    public AccountDTO getSingle(@PathVariable("accountId") Long accountId) {
        return service.getByAccountId(accountId);
    }
}
