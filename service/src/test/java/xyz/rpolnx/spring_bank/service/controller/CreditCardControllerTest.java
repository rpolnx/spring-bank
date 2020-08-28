package xyz.rpolnx.spring_bank.service.controller;

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
import xyz.rpolnx.spring_bank.common.config.GlobalExceptionHandler;
import xyz.rpolnx.spring_bank.common.config.SerializationConfig;
import xyz.rpolnx.spring_bank.common.model.dto.integration.CreditCardDTO;
import xyz.rpolnx.spring_bank.service.external.CreditCardRepository;
import xyz.rpolnx.spring_bank.service.model.entity.CreditCard;
import xyz.rpolnx.spring_bank.service.model.factory.CreditCardDTOFactory;
import xyz.rpolnx.spring_bank.service.service.ScoreCategoryService;
import xyz.rpolnx.spring_bank.service.service.impl.CreditCardServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ContextConfiguration(classes = {CreditCardController.class, GlobalExceptionHandler.class, SerializationConfig.class})
@TestPropertySource(properties = "logging.config=../logback-spring.xml")
@WebMvcTest
public class CreditCardControllerTest {
    @MockBean
    private ScoreCategoryService scoreCategoryService;
    @MockBean
    private CreditCardRepository repository;

    @SpyBean
    private CreditCardServiceImpl service;

    @Autowired
    private MockMvc request;
    @Autowired
    private ObjectMapper mapper;

    @SneakyThrows
    @Test
    @DisplayName("When getting all cards, should return 200 and card list")
    public void getAllCards() {
        List<CreditCard> accounts = CreditCardMocks.generateCreditCards(5, 10);

        when(repository.findAll()).thenReturn(accounts);

        List<CreditCardDTO> response = accounts.stream().map(CreditCardDTOFactory::fromEntity).collect(Collectors.toList());

        this.request.perform(get("/credit-cards")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(response)));
    }

    @SneakyThrows
    @Test
    @DisplayName("When getting cards from one accountId, should return 200 and card list")
    public void getCardsByAccountId() {
        Long id = 12345670L;

        List<CreditCard> accounts = CreditCardMocks.generateCreditCards(5, 1000);

        when(repository.findAllByAccountIdAndDeletedOnIsNull(id)).thenReturn(accounts);

        List<CreditCardDTO> response = accounts.stream().map(CreditCardDTOFactory::fromEntity).collect(Collectors.toList());

        this.request.perform(get("/accounts/{accountId}/credit-cards", id)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(response)));
    }

    @SneakyThrows
    @Test
    @DisplayName("When there is no card from this accountId, should return 200 empty list")
    public void getEmptyCardList() {
        Long id = 12345670L;

        when(repository.findAllByAccountIdAndDeletedOnIsNull(id)).thenReturn(List.of());

        this.request.perform(get("/accounts/{accountId}/credit-cards", id)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(List.of())));
    }
}