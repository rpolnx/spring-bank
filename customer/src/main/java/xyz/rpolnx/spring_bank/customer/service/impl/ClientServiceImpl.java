package xyz.rpolnx.spring_bank.customer.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.rpolnx.spring_bank.common.exceptions.ConflictException;
import xyz.rpolnx.spring_bank.common.exceptions.NotFoundException;
import xyz.rpolnx.spring_bank.common.model.dto.CustomerEvent;
import xyz.rpolnx.spring_bank.common.model.enums.EventType;
import xyz.rpolnx.spring_bank.customer.external.ClientPublisher;
import xyz.rpolnx.spring_bank.customer.external.ClientRepository;
import xyz.rpolnx.spring_bank.customer.model.dto.ClientDTO;
import xyz.rpolnx.spring_bank.customer.model.entity.Client;
import xyz.rpolnx.spring_bank.customer.model.factory.CustomerEventFactory;
import xyz.rpolnx.spring_bank.customer.service.ClientService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static xyz.rpolnx.spring_bank.common.model.enums.EventType.*;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientRepository repository;
    private final ClientPublisher publisher;

    private static final int MAX_SCORE_NUMBER = 10;

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
        client.validate();

        Optional<Client> exists = repository.findById(client.getDocumentNumber());

        if (exists.isPresent()) {
            throw new ConflictException("Duplicated client");
        }

        int score = (int) (Math.random() * MAX_SCORE_NUMBER);

        Client entity = new Client(client.getDocumentNumber(), client.getFullName(), client.getPersonType(), score);

        Client created = repository.save(entity);

        CustomerEvent event = CustomerEventFactory.generateCustomer(created, CREATION);

        publisher.handleClientEvent(event);

        return ClientDTO.withOnlyDocumentNumber(created.getDocumentNumber());
    }

    @Override
    @Transactional
    public void update(ClientDTO dto, String documentNumber) {
        dto.validate();

        Client entity = repository.findById(documentNumber)
                .orElse(new Client(documentNumber, dto.getFullName(), dto.getPersonType(), dto.getScore()));

        Client updatedEntity = entity.withNewValues(dto.getFullName(), dto.getPersonType(), dto.getScore());

        Client updated = repository.save(updatedEntity);

        CustomerEvent event = CustomerEventFactory.generateCustomer(updated, UPDATE);

        publisher.handleClientEvent(event);
    }

    @Override
    public void delete(String documentNumber) {
        Client client = new Client().withDocumentNumber(documentNumber);

        repository.delete(client);

        CustomerEvent event = CustomerEventFactory.generateCustomer(client, DELETE);

        publisher.handleClientEvent(event);
    }
}
