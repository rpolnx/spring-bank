package xyz.rpolnx.spring_bank.account.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import xyz.rpolnx.spring_bank.common.model.dto.integration.CreditCardDTO;

import java.util.List;

@FeignClient(name = "CreditCardApi", url = "${service.ms.host}")
public interface CreditCardApi {
    @GetMapping("credit-cards")
    List<CreditCardDTO> getAll();

    @GetMapping("accounts/{accountId}/credit-cards")
    List<CreditCardDTO> getSingle(@PathVariable("accountId") Long accountId);
}
