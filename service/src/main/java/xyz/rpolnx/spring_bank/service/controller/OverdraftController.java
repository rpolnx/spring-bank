package xyz.rpolnx.spring_bank.service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import xyz.rpolnx.spring_bank.service.model.dto.OverdraftDTO;
import xyz.rpolnx.spring_bank.service.service.OverdraftService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OverdraftController {
    private final OverdraftService service;

    @GetMapping("overdrafts")
    public List<OverdraftDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("accounts/{accountId}/overdrafts")
    public OverdraftDTO getSingle(@PathVariable("accountId") Long accountId) {
        return service.getByAccountId(accountId);
    }
}
