package xyz.rpolnx.spring_bank.customer.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import xyz.rpolnx.spring_bank.customer.exceptions.NotFoundException;
import xyz.rpolnx.spring_bank.customer.external.ClientPublisher;
import xyz.rpolnx.spring_bank.customer.external.ClientRepository;
import xyz.rpolnx.spring_bank.customer.model.PersonType;
import xyz.rpolnx.spring_bank.customer.model.dto.ClientDTO;
import xyz.rpolnx.spring_bank.customer.model.entity.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceImplTest {

    private static final int MAX_SCORE_NUMBER = 10;

    @Mock
    private ClientRepository repository;
    @Mock
    private ClientPublisher publisher;

    @InjectMocks
    private ClientServiceImpl service;

    @Test
    @DisplayName("When database has clients, should return them all")
    public void shouldGetAllClients() {
        List<Client> clients = generateClients(15);
        when(repository.findAll()).thenReturn(clients);

        List<ClientDTO> actualClients = service.getAll();

        assertFalse(actualClients.isEmpty());
        assertEquals("Name #13", actualClients.get(13).getFullName());
        assertEquals("6", actualClients.get(6).getDocumentNumber());
    }

    @Test
    @DisplayName("When database has no clients, should return empty list")
    public void shouldGetAllClientsWithEmptyList() {
        List<Client> clients = generateClients(15);
        when(repository.findAll()).thenReturn(new ArrayList<>());

        List<ClientDTO> actualClients = service.getAll();

        assertTrue(actualClients.isEmpty());
    }

    @Test
    @DisplayName("When getting client by id, should return it")
    public void shouldGetSingleClient() {
        String documentNumber = "1234";

        Client client = new Client(documentNumber, "", PersonType.PF, 8);
        when(repository.findById(anyString())).thenReturn(Optional.of(client));

        ClientDTO actualClients = assertDoesNotThrow(() -> service.get(documentNumber));

        assertNotNull(actualClients);
        assertEquals("1234", actualClients.getDocumentNumber());
    }

    @Test
    @DisplayName("When getting user and it does not exist, should throw error")
    public void shouldThrowErrorWhenUserNotFound() {
        when(repository.findById(anyString())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> service.get("no-user"));

        assertTrue(exception.getMessage().contains("Client not found"));
    }

    @Test
    @DisplayName("When updating user, should not throw exception")
    public void shouldUpdateUser() {
        String documentNumber = "1234";

        ClientDTO request = new ClientDTO("not usable", "", PersonType.PF, 8);
        Client client = new Client("1234", "", PersonType.PF, 8);

        assertDoesNotThrow(() -> service.update(request, documentNumber));
        verify(repository, times(1)).save(client);
    }

    @Test
    @DisplayName("When deleting user, should not throw exception")
    public void shouldDeleteUser() {
        String documentNumber = "1234";

        assertDoesNotThrow(() -> service.delete(documentNumber));
        verify(repository, times(1)).deleteById(documentNumber);
    }

    private List<Client> generateClients(int size) {
        List<Client> clients = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            clients.add(generateClient(i));
        }
        return clients;
    }

    private Client generateClient(int position) {
        int typePositionRandom = (int) (Math.random() * PersonType.values().length);
        PersonType type = PersonType.values()[typePositionRandom];

        int randomScore = (int) (Math.random() * MAX_SCORE_NUMBER);

        return new Client(String.valueOf(position), "Name #" + position, type, randomScore);
    }

}