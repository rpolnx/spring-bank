package xyz.rpolnx.spring_bank.customer.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.rpolnx.spring_bank.customer.exceptions.NotFoundException;
import xyz.rpolnx.spring_bank.customer.external.ClientPublisher;
import xyz.rpolnx.spring_bank.customer.external.ClientRepository;
import xyz.rpolnx.spring_bank.customer.model.dto.ClientDTO;
import xyz.rpolnx.spring_bank.customer.model.entity.Client;
import xyz.rpolnx.spring_bank.customer.service.ClientService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientRepository repository;
    private final ClientPublisher publisher;

    @Override
    public List<ClientDTO> getAll() {
        return repository.findAll().stream()
                .map(ClientDTO::of)
                .collect(Collectors.toList());
    }

    @Override
    public ClientDTO get(String documentNumber) {
        return repository.findById(documentNumber)
                .map(ClientDTO::of)
                .orElseThrow(() -> new NotFoundException("Client not found"));
    }

    @Override
    @Transactional
    public ClientDTO create(ClientDTO client) {
        Client entity = new Client(client.getDocumentNumber(), client.getFullName(), client.getPersonType(), client.getScore());

        Client created = repository.save(entity);

        ClientDTO clientDTO = ClientDTO.of(created);

        publisher.handleClientCreation(clientDTO);

        return clientDTO;
    }

    @Override
    public void update(ClientDTO client, String documentNumber) {
        Client entity = new Client(documentNumber, client.getFullName(), client.getPersonType(), client.getScore());

        repository.save(entity);
    }

    @Override
    public void delete(String documentNumber) {
        repository.deleteById(documentNumber);
    }
}
