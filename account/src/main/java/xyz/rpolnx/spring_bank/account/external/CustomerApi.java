package xyz.rpolnx.spring_bank.account.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import xyz.rpolnx.spring_bank.common.model.dto.CustomerDTO;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@FeignClient(name = "CustomerApi", url = "${customer.ms.host}")
public interface CustomerApi {
    @GetMapping("clients")
    @ResponseStatus(OK)
    public List<CustomerDTO> getAll();

    @GetMapping("/clients/{documentNumber}")
    @ResponseStatus(OK)
    public CustomerDTO get(@PathVariable("documentNumber") String documentNumber);
}
