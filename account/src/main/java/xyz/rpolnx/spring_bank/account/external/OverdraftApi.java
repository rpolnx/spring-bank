package xyz.rpolnx.spring_bank.account.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import xyz.rpolnx.spring_bank.common.model.dto.integration.OverdraftDTO;

import java.util.List;

@FeignClient(name = "OverdraftApi", url = "${service.ms.host}")
public interface OverdraftApi {
    @RequestMapping("overdrafts")
    public List<OverdraftDTO> getAll();

    @RequestMapping("accounts/{accountId}/overdrafts")
    public List<OverdraftDTO> getSingle(@PathVariable("accountId") Long accountId);
}
