package xyz.rpolnx.spring_bank.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.rpolnx.spring_bank.common.model.dto.AccountEvent;
import xyz.rpolnx.spring_bank.common.model.dto.CustomerEvent;
import xyz.rpolnx.spring_bank.common.util.RandomGeneratorUtils;
import xyz.rpolnx.spring_bank.service.external.CreditCardRepository;
import xyz.rpolnx.spring_bank.service.model.entity.CreditCard;
import xyz.rpolnx.spring_bank.service.model.entity.ScoreCategory;
import xyz.rpolnx.spring_bank.service.model.factory.CreditCardBuilder;
import xyz.rpolnx.spring_bank.service.service.CreditCardService;
import xyz.rpolnx.spring_bank.service.service.ScoreCategoryService;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static java.util.Objects.isNull;

@RequiredArgsConstructor
@Service
@Log4j2
public class CreditCardServiceImpl implements CreditCardService {
    private final ScoreCategoryService scoreCategoryService;
    private final CreditCardRepository repository;

    private static final Integer MAX_SECURITY_CODE_DIGITS = 3;
    private static final Integer CARD_NUMBER_OF_RANDOM_NUMBERS = 15;
    private static final String CARD_BRAND_PREFIX = "9";
    private static final String CARD_BRAND_NAME = "My brand";
    private static final LocalDate expiration = LocalDate.now().plusYears(5);

    @Override
    @Transactional
    public void createCreditCard(AccountEvent event) {
        ScoreCategory category = scoreCategoryService.getActiveCategory(event.getCustomer().getScore());
        Double creditCardLimit = category.getCreditCardLimit();

        if (isNull(creditCardLimit) || creditCardLimit == 0) {
            log.info("ClientId {} with accountId {} don't generate card", event.getCustomer().getId(), event.getAccount().getId());
            return;
        }

        CreditCard creditCard = generateCreditCard(event, category);

        CreditCard saved = repository.save(creditCard);

        log.info("Credit card created for accountId {}", event.getAccount().getId());
    }

    @Override
    @Transactional
    public void updateCreditCard(AccountEvent event) {
        ScoreCategory category = scoreCategoryService.getActiveCategory(event.getCustomer().getScore());

        repository.findAllByAccountIdAndDeletedOnIsNull(event.getAccount().getId())
                .forEach(it -> {
                    if (!it.getScoreCategory().equals(category)) {
                        it.setScoreCategory(category);

                        Double limitDifference = it.getScoreCategory().getCreditCardLimit();
                        double availableLimit = limitDifference - it.getRemainingLimit();

                        it.setRemainingLimit(availableLimit);
                        log.info("Credit card with id {} updated for accountId {}", it.getNumber(), event.getAccount().getId());
                    }
                });
        log.info("Updated credit cards for accountId {}", event.getAccount().getId());
    }

    @Override
    @Transactional
    public void deleteCreditCard(AccountEvent event) {
        repository.findAllByAccountIdAndDeletedOnIsNull(event.getAccount().getId())
                .forEach(it -> {
                    it.setDeletedOn(LocalDateTime.now());
                    log.info("Inactivating Credit card with id {} updated for accountId {}", it.getNumber(),
                            event.getAccount().getId());
                });
        log.info("Credit cards for accountId {} inactivated", event.getAccount().getId());
    }

    private CreditCard generateCreditCard(AccountEvent event, ScoreCategory category) {
        CustomerEvent.Customer customer = event.getCustomer();
        AccountEvent.Account account = event.getAccount();

        String cardNumber = RandomGeneratorUtils.generateSizedRandomNumbers(CARD_NUMBER_OF_RANDOM_NUMBERS, CARD_BRAND_PREFIX);
        String securityCode = RandomGeneratorUtils.generateSizedRandomNumbers(MAX_SECURITY_CODE_DIGITS, "");

        return CreditCardBuilder.builder()
                .withAccountInfo(account)
                .withCustomerInfo(customer)
                .withScoreCategory(category)
                .withCardInfo(cardNumber, CARD_BRAND_NAME, securityCode, expiration)
                .build();
    }
}
