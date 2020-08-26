package xyz.rpolnx.spring_bank.customer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.rpolnx.spring_bank.customer.model.dto.ClientDTO;
import xyz.rpolnx.spring_bank.customer.repository.ClientRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientRepository repository;

    @Override
    public List<ClientDTO> getAll() {
        return repository.findAll();
    }

    @Override
    public ClientDTO get(String documentNumber) {
        return repository.findById(documentNumber);
    }

    @Override
    public ClientDTO create(ClientDTO client) {
        return repository.save(client);
    }

    @Override
    public void update(ClientDTO client, String documentNumber) {

    }

    @Override
    public void delete(String documentNumber) {

    }
}
