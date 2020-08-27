package xyz.rpolnx.spring_bank.customer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import xyz.rpolnx.spring_bank.customer.model.dto.ClientDTO;
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
    public List<ClientDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/{documentNumber}")
    @ResponseStatus(OK)
    public ClientDTO get(@PathVariable("documentNumber") String documentNumber) {
        return service.get(documentNumber);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public ClientDTO create(@Valid @RequestBody ClientDTO clientDTO) {
        return service.create(clientDTO);
    }

    @PutMapping("/{documentNumber}")
    @ResponseStatus(NO_CONTENT)
    public void update(@Valid @RequestBody ClientDTO clientDTO, @PathVariable("documentNumber") String documentNumber) {
        service.update(clientDTO, documentNumber);
    }

    @DeleteMapping("/{documentNumber}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable("documentNumber") String documentNumber) {
        service.delete(documentNumber);
    }

}
