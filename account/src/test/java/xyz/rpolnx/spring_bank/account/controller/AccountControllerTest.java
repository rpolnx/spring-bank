package xyz.rpolnx.spring_bank.account.controller;

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
import xyz.rpolnx.spring_bank.account.external.*;
import xyz.rpolnx.spring_bank.account.model.dto.AccountDTO;
import xyz.rpolnx.spring_bank.account.model.entity.Account;
import xyz.rpolnx.spring_bank.account.model.factory.AccountDTOFactory;
import xyz.rpolnx.spring_bank.account.service.impl.AccountServiceImpl;
import xyz.rpolnx.spring_bank.common.config.GlobalExceptionHandler;
import xyz.rpolnx.spring_bank.common.config.SerializationConfig;
import xyz.rpolnx.spring_bank.common.exceptions.NotFoundException;
import xyz.rpolnx.spring_bank.common.model.dto.CustomerDTO;
import xyz.rpolnx.spring_bank.common.model.dto.integration.CreditCardDTO;
import xyz.rpolnx.spring_bank.common.model.dto.integration.OverdraftDTO;
import xyz.rpolnx.spring_bank.common.model.enums.CustomPersonType;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ContextConfiguration(classes = {AccountController.class, GlobalExceptionHandler.class, SerializationConfig.class})
@TestPropertySource(properties = "logging.config=../logback-spring.xml")
@WebMvcTest
public class AccountControllerTest {
    @MockBean
    private AccountRepository repository;
    @MockBean
    private AccountPublisher publisher;
    @MockBean
    private CreditCardApi creditCardApi;
    @MockBean
    private OverdraftApi overdraftApi;
    @MockBean
    private CustomerApi customerApi;

    @SpyBean
    private AccountServiceImpl service;

    @Autowired
    private MockMvc request;
    @Autowired
    private ObjectMapper mapper;

    @SneakyThrows
    @Test
    @DisplayName("When getting client, should return 200 and client list")
    public void getAllClients() {
        List<Account> accounts = AccountMocks.generateAccounts(5, 10);

        when(repository.findAll()).thenReturn(accounts);
        CustomerDTO customerDTO = new CustomerDTO("", "", CustomPersonType.PF);

        List<CreditCardDTO> creditCards = List.of(new CreditCardDTO("12", 123L, "Test",
                "brand name", "123", LocalDate.now(), 250d));

        List<OverdraftDTO> overdrafts = List.of(new OverdraftDTO(123L, 250d));

        when(customerApi.get(anyString())).thenReturn(customerDTO);
        when(creditCardApi.getSingle(anyLong())).thenReturn(creditCards);
        when(overdraftApi.getSingle(anyLong())).thenReturn(overdrafts);

        List<AccountDTO> response = accounts.stream()
                .map(AccountDTOFactory::fromEntity)
                .map(it -> it.withCustomer(customerDTO))
                .map(it -> it.withCards(creditCards))
                .map(it -> it.withOverdrafts(overdrafts))
                .collect(Collectors.toList());

        this.request.perform(get("/accounts")
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

        Account account = AccountMocks.generateAccounts(1, 1).get(0);

        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(account));

        CustomerDTO customerDTO = new CustomerDTO();
        List<CreditCardDTO> creditCards = List.of();
        List<OverdraftDTO> overdrafts = List.of();

        AccountDTO accountDTO = AccountDTOFactory.fromEntity(account);

        AccountDTO response = new AccountDTO(accountDTO.getAccount(), customerDTO, creditCards, overdrafts);

        when(customerApi.get(anyString())).thenReturn(customerDTO);
        when(creditCardApi.getSingle(anyLong())).thenReturn(creditCards);
        when(overdraftApi.getSingle(anyLong())).thenReturn(overdrafts);

        this.request.perform(get("/accounts/{id}", id)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(response)));
    }

    @SneakyThrows
    @Test
    @DisplayName("When client not found, should return 404 and client")
    public void shouldFindANotFoundClient() {
        String id = "12345678910";


        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        String contentAsString = this.request.perform(get("/accounts/{id}", id)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        assertTrue(contentAsString.contains("Account not found by id"));
    }

    @SneakyThrows
    @Test
    @DisplayName("When client not found, should return 404 and client")
    public void shouldFindAnEmptyClient() {
        String id = "12345678910";

        Account account = AccountMocks.generateAccounts(1, 1).get(0);

        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(account));

        CustomerDTO customerDTO = new CustomerDTO();
        List<CreditCardDTO> creditCards = List.of();
        List<OverdraftDTO> overdrafts = List.of();

        AccountDTO accountDTO = AccountDTOFactory.fromEntity(account);

        AccountDTO response = new AccountDTO(accountDTO.getAccount(), customerDTO, creditCards, overdrafts);

        given(customerApi.get(anyString())).willAnswer(invocation -> {
            throw new NotFoundException("ClientNotFound");
        });

        when(creditCardApi.getSingle(anyLong())).thenReturn(creditCards);
        when(overdraftApi.getSingle(anyLong())).thenReturn(overdrafts);

        this.request.perform(get("/accounts/{id}", id)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(response)));
    }
}