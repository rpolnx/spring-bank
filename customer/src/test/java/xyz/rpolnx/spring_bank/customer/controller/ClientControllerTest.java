package xyz.rpolnx.spring_bank.customer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
import xyz.rpolnx.spring_bank.common.config.GlobalExceptionHandler;
import xyz.rpolnx.spring_bank.common.config.SerializationConfig;
import xyz.rpolnx.spring_bank.customer.external.ClientPublisher;
import xyz.rpolnx.spring_bank.customer.external.ClientRepository;
import xyz.rpolnx.spring_bank.customer.mocks.ClientMock;
import xyz.rpolnx.spring_bank.customer.model.PersonType;
import xyz.rpolnx.spring_bank.customer.model.dto.ClientDTO;
import xyz.rpolnx.spring_bank.customer.model.dto.ClientEvent;
import xyz.rpolnx.spring_bank.customer.model.entity.Client;
import xyz.rpolnx.spring_bank.customer.service.impl.ClientServiceImpl;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static xyz.rpolnx.spring_bank.common.model.enums.EventType.*;

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
    public void getAllClients() {
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
    public void getClient() {
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
    public void notFoundClient() {
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
    public void createClient() {

        ClientDTO request = new ClientDTO("01234567891", "Client", PersonType.PF);
        Client client = new Client("01234567891", "Client", PersonType.PF, 8);
        ClientEvent event = ClientEvent.of(client, CREATION);

        when(repository.findById(anyString())).thenReturn(Optional.empty());
        when(repository.save(any(Client.class))).thenReturn(client);

        this.request.perform(post("/clients")
                .content(mapper.writeValueAsBytes(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(ClientDTO.withOnlyDocumentNumber(client.getDocumentNumber()))));

        verify(publisher).handleClientEvent(event);
    }

    @SneakyThrows
    @Test
    @DisplayName("When creating an existing document number, should return 200 and created client id")
    public void createDuplicatedClient() {

        ClientDTO request = new ClientDTO("01234567891", "Client", PersonType.PF);
        Client client = new Client("01234567891", "Client", PersonType.PF, 2);

        when(repository.findById(anyString())).thenReturn(Optional.of(client));

        String message = this.request.perform(post("/clients")
                .content(mapper.writeValueAsBytes(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        assertTrue(message.contains("Duplicated client"));
    }

    @SneakyThrows
    @Test
    @DisplayName("When updating an existing document number, should modify only content and return 204 status")
    public void updateDuplicatedClient() {

        String id = "98765432101";

        ClientDTO request = new ClientDTO("01234567891", "Client", PersonType.PF, 7);
        Client client = new Client("01234567891", "Client", PersonType.PF, 2);
        Client newClient = new Client("01234567891", "Client", PersonType.PF, 7);

        when(repository.findById(anyString())).thenReturn(Optional.of(client));
        when(repository.save(newClient)).thenReturn(client);

        this.request.perform(put("/clients/{id}", id)
                .content(mapper.writeValueAsBytes(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        ClientEvent event = ClientEvent.of(newClient, UPDATE);

        verify(publisher).handleClientEvent(event);
    }

    @SneakyThrows
    @Test
    @DisplayName("When get single client, should return 200 and client")
    public void deleteClient() {
        String id = "12345678910";
        Client client = new Client().withDocumentNumber(id);
        ClientEvent event = ClientEvent.of(client, DELETE);

        this.request.perform(delete("/clients/{id}", id)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(repository).delete(client);
        verify(publisher).handleClientEvent(event);
    }

    @SneakyThrows
    @Test
    @DisplayName("When creating an existing document number, should return 200 and created client id")
    public void createClientWithEmptyBody() {
        String message = this.request.perform(post("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        assertTrue(message.contains("Required request body is missing"));
    }

    @SneakyThrows
    @Test
    @DisplayName("When creating client with wrong listed enum, should return unprocessable entity with message")
    public void createClientWithWrongPersonType() {
        ClientDTO request = new ClientDTO("abc", "Client", PersonType.PJ);
        byte[] content = mapper.writeValueAsBytes(request);

        ObjectNode node = (ObjectNode) mapper.readTree(content);
        node.put("personType", "NO_ENUM");

        byte[] requestContent = mapper.writeValueAsBytes(node);

        String message = this.request.perform(post("/clients")
                .content(requestContent)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        ObjectNode exceptionWrapper = mapper.readValue(message, ObjectNode.class);
        assertNotNull(exceptionWrapper);
        assertTrue(exceptionWrapper.at("/message").textValue().contains("JSON parse error: Cannot deserialize value"));
        assertTrue(exceptionWrapper.at("/message").textValue().contains("not one of the values accepted for Enum class: [PJ, PF];"));
    }

    @SneakyThrows
    @Test
    @DisplayName("When creating client with wrong type, should return unprocessable entity with message")
    public void createClientWithTypeError() {
        ClientDTO request = new ClientDTO("01234567891", "Client", PersonType.PJ);
        byte[] content = mapper.writeValueAsBytes(request);

        ObjectNode node = (ObjectNode) mapper.readTree(content);
        ArrayNode arrayNode = mapper.createArrayNode();
        node.set("fullName", arrayNode);

        byte[] requestContent = mapper.writeValueAsBytes(node);

        String message = this.request.perform(post("/clients")
                .content(requestContent)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        ObjectNode exceptionWrapper = mapper.readValue(message, ObjectNode.class);
        assertNotNull(exceptionWrapper);
        assertTrue(exceptionWrapper.at("/message").textValue().contains("fullName"));
        assertTrue(exceptionWrapper.at("/message").textValue().contains("Cannot deserialize instance"));
    }

    @SneakyThrows
    @Test
    @DisplayName("When creating client with wrong document number pattern, should return bad request with message")
    public void createClientWithWrongPatternId() {
        ClientDTO request = new ClientDTO("56789", "Client", PersonType.PF);

        String message = this.request.perform(post("/clients")
                .content(mapper.writeValueAsBytes(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        ObjectNode exceptionWrapper = mapper.readValue(message, ObjectNode.class);
        assertNotNull(exceptionWrapper);
        assertTrue(exceptionWrapper.at("/specificErrors/0/defaultMessage").textValue()
                .contains("must match \"^\\d{11,14}$\""));
        assertEquals("documentNumber", exceptionWrapper.at("/specificErrors/0/field").textValue());
    }

    @SneakyThrows
    @Test
    @DisplayName("When creating client with wrong document number pattern, should return bad request with message")
    public void createClientWithWrongSizeOfPJ() {
        ClientDTO request = new ClientDTO("123456789101", "Client", PersonType.PJ);

        String message = this.request.perform(post("/clients")
                .content(mapper.writeValueAsBytes(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        ObjectNode exceptionWrapper = mapper.readValue(message, ObjectNode.class);
        assertNotNull(exceptionWrapper);
        assertEquals("PJ must has document size of 14", exceptionWrapper.at("/message").textValue());
    }

    @SneakyThrows
    @Test
    @DisplayName("When creating client and got sql problem, should return internal server error")
    public void createClientGotSQLProblem() {
        ClientDTO request = new ClientDTO("12345678910", "Client", PersonType.PF);

        given(repository.save(any(Client.class))).willAnswer(invocation -> {
            throw new SQLException("SQL Error");
        });

        String message = this.request.perform(post("/clients")
                .content(mapper.writeValueAsBytes(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        ObjectNode exceptionWrapper = mapper.readValue(message, ObjectNode.class);
        assertNotNull(exceptionWrapper);
        assertEquals("SQL Error", exceptionWrapper.at("/message").textValue());
    }

    @SneakyThrows
    @Test
    @DisplayName("When creating client and got unexpected problem, should return internal server error")
    public void createClientGotUnexpectedProblem() {
        ClientDTO request = new ClientDTO("12345678910", "Client", PersonType.PF);

        given(repository.save(any(Client.class))).willAnswer(invocation -> {
            throw new Exception("Unexpected error");
        });

        String message = this.request.perform(post("/clients")
                .content(mapper.writeValueAsBytes(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        ObjectNode exceptionWrapper = mapper.readValue(message, ObjectNode.class);
        assertNotNull(exceptionWrapper);
        assertEquals("Unexpected error", exceptionWrapper.at("/message").textValue());
    }
}
