package xyz.rpolnx.spring_bank.service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import xyz.rpolnx.spring_bank.common.model.dto.integration.CreditCardDTO;
import xyz.rpolnx.spring_bank.service.service.CreditCardService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CreditCardController {
    private final CreditCardService service;

    @GetMapping("credit-cards")
    public List<CreditCardDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("accounts/{accountId}/credit-cards")
    public List<CreditCardDTO> getSingle(@PathVariable("accountId") Long accountId) {
        return service.getByAccountId(accountId);
    }
}
