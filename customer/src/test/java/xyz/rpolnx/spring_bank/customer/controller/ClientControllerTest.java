package xyz.rpolnx.spring_bank.customer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import xyz.rpolnx.spring_bank.customer.config.GlobalExceptionHandler;
import xyz.rpolnx.spring_bank.customer.config.SerializationConfig;
import xyz.rpolnx.spring_bank.customer.external.ClientPublisher;
import xyz.rpolnx.spring_bank.customer.external.ClientRepository;
import xyz.rpolnx.spring_bank.customer.mocks.ClientMock;
import xyz.rpolnx.spring_bank.customer.model.PersonType;
import xyz.rpolnx.spring_bank.customer.model.dto.ClientDTO;
import xyz.rpolnx.spring_bank.customer.model.entity.Client;
import xyz.rpolnx.spring_bank.customer.service.impl.ClientServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ContextConfiguration(classes = {ClientController.class, GlobalExceptionHandler.class, SerializationConfig.class})
@TestPropertySource(properties = "logging.config=../logback-spring.xml")
@WebMvcTest
public class ClientControllerTest {

    @MockBean
    private ClientRepository repository;
    @MockBean
    private ClientPublisher publisher;

    @SpyBean
    private ClientServiceImpl service;

    @Autowired
    private MockMvc request;
    @Autowired
    private ObjectMapper mapper;

    @SneakyThrows
    @Test
    @DisplayName("Should return 404 when path not found")
    public void ShouldReturnNotFound() {
        this.request.perform(get("/not-found"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("")));
    }

    @SneakyThrows
    @Test
    @DisplayName("When getting client, should return 200 and client list")
    public void getAllUsers() {
        List<Client> clients = ClientMock.generateClients(5, 10);

        when(repository.findAll()).thenReturn(clients);

        List<ClientDTO> response = clients.stream().map(ClientDTO::new).collect(Collectors.toList());

        this.request.perform(get("/clients")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(response)));
    }


    @SneakyThrows
    @Test
    @DisplayName("When get single client, should return 200 and client")
    public void getUser() {
        String id = "12345678910";

        Client client = new Client(id, "Client", PersonType.PF, 7);
        when(repository.findById(id)).thenReturn(Optional.of(client));

        ClientDTO response = ClientDTO.of(client);

        this.request.perform(get("/clients/{id}", id)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(response)));
    }

    @SneakyThrows
    @Test
    @DisplayName("When get single client and not found, should return 404")
    public void notFoundUser() {
        String id = "12345678910";

        when(repository.findById(id)).thenReturn(Optional.empty());

        String message = this.request.perform(get("/clients/{id}", id)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        assertTrue(message.contains("Client not found"));
    }

    @SneakyThrows
    @Test
    @DisplayName("When creating client, should return 200 and created client id")
    public void createUser() {

        ClientDTO request = new ClientDTO("01234567891", "Client", PersonType.PF, 2);
        Client client = new Client("01234567891", "Client", PersonType.PF, 2);

        when(repository.findById(anyString())).thenReturn(Optional.empty());
        when(repository.save(any(Client.class))).thenReturn(client);

        this.request.perform(post("/clients")
                .content(mapper.writeValueAsBytes(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(ClientDTO.of(client).onlyDocumentNumber())));
    }

    @SneakyThrows
    @Test
    @DisplayName("When creating an existing document number, should return 200 and created client id")
    public void createDuplicatedUser() {

        ClientDTO request = new ClientDTO("01234567891", "Client", PersonType.PF, 2);
        Client client = new Client("01234567891", "Client", PersonType.PF, 2);

        when(repository.findById(anyString())).thenReturn(Optional.of(client));
        doNothing().when(publisher).handleClientCreation(request);

        String message = this.request.perform(post("/clients")
                .content(mapper.writeValueAsBytes(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        assertTrue(message.contains("Duplicated user"));
    }

    @SneakyThrows
    @Test
    @DisplayName("When updating an existing document number, should modify only content and return 204 status")
    public void updateDuplicatedUser() {

        String id = "98765432101";

        ClientDTO request = new ClientDTO("01234567891", "Client", PersonType.PF, 2);
        Client client = new Client("01234567891", "Client", PersonType.PF, 2);

        when(repository.findById(anyString())).thenReturn(Optional.of(client));

        this.request.perform(put("/clients/{id}", id)
                .content(mapper.writeValueAsBytes(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }


}