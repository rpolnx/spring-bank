package xyz.rpolnx.spring_bank.customer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import xyz.rpolnx.spring_bank.customer.model.dto.ClientDTO;
import xyz.rpolnx.spring_bank.customer.service.ClientService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService service;

    @GetMapping
    public List<ClientDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/{documentNumber}")
    public ClientDTO get(@PathVariable("documentNumber") String documentNumber) {
        return service.get(documentNumber);
    }

    @PostMapping
    public ClientDTO create(@Valid @RequestBody ClientDTO clientDTO) {
        return service.create(clientDTO);
    }

    @PutMapping("/{documentNumber}")
    public void update(@Valid @RequestBody ClientDTO clientDTO, @PathVariable("documentNumber") String documentNumber) {
        service.update(clientDTO, documentNumber);
    }

    @DeleteMapping("/{documentNumber}")
    public void delete(@PathVariable("documentNumber") String documentNumber) {
        service.delete(documentNumber);
    }

}
