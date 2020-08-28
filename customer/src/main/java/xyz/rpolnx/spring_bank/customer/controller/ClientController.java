package xyz.rpolnx.spring_bank.customer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import xyz.rpolnx.spring_bank.common.model.dto.CustomerDTO;
import xyz.rpolnx.spring_bank.customer.service.ClientService;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService service;

    @GetMapping
    @ResponseStatus(OK)
    public List<CustomerDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/{documentNumber}")
    @ResponseStatus(OK)
    public CustomerDTO get(@PathVariable("documentNumber") String documentNumber) {
        return service.get(documentNumber);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public CustomerDTO create(@Valid @RequestBody CustomerDTO customerDTO) {
        return service.create(customerDTO);
    }

    @PutMapping("/{documentNumber}")
    @ResponseStatus(NO_CONTENT)
    public void update(@Valid @RequestBody CustomerDTO customerDTO, @PathVariable("documentNumber") String documentNumber) {
        service.update(customerDTO, documentNumber);
    }

    @DeleteMapping("/{documentNumber}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable("documentNumber") String documentNumber) {
        service.delete(documentNumber);
    }

}
