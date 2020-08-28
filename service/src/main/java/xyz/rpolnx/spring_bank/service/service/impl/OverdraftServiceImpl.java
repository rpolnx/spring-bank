package xyz.rpolnx.spring_bank.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.rpolnx.spring_bank.common.model.dto.AccountEvent;
import xyz.rpolnx.spring_bank.common.model.dto.CustomerEvent;
import xyz.rpolnx.spring_bank.service.external.OverdraftRepository;
import xyz.rpolnx.spring_bank.service.model.entity.Overdraft;
import xyz.rpolnx.spring_bank.service.model.entity.ScoreCategory;
import xyz.rpolnx.spring_bank.service.service.OverdraftService;
import xyz.rpolnx.spring_bank.service.service.ScoreCategoryService;

import java.time.LocalDateTime;

import static java.util.Objects.isNull;

@RequiredArgsConstructor
@Service
@Log4j2
public class OverdraftServiceImpl implements OverdraftService {
    private final ScoreCategoryService scoreCategoryService;
    private final OverdraftRepository repository;

    @Override
    @Transactional
    public void createOverdraft(AccountEvent event) {
        CustomerEvent.Customer customer = event.getCustomer();
        AccountEvent.Account account = event.getAccount();

        ScoreCategory category = scoreCategoryService.getActiveCategory(customer.getScore());
        Double overdraftLimit = category.getOverdraftLimit();

        if (isNull(overdraftLimit) || overdraftLimit == 0) {
            log.info("ClientId {} with accountId {} don't generate overdraft", customer.getId(), account.getId());
            return;
        }

        Overdraft overdraft = Overdraft.builder()
                .accountId(account.getId())
                .remainingLimit(category.getOverdraftLimit())
                .scoreCategory(category)
                .build();

        Overdraft saved = repository.save(overdraft);

        log.info("Credit card created for accountId {}", account.getId());
    }

    @Override
    @Transactional
    public void updateOverdraft(AccountEvent event) {
        ScoreCategory category = scoreCategoryService.getActiveCategory(event.getCustomer().getScore());

        AccountEvent.Account account = event.getAccount();
        repository.findAllByAccountIdAndDeletedOnIsNull(account.getId())
                .forEach(it -> {
                    if (!it.getScoreCategory().equals(category)) {
                        it.setScoreCategory(category);

                        Double limitDifference = category.getOverdraftLimit() - it.getScoreCategory().getOverdraftLimit();
                        double availableLimit = limitDifference + it.getRemainingLimit();

                        it.setRemainingLimit(availableLimit > 0 ? availableLimit : 0);
                        log.info("Overdraft created for accountId {}", event.getAccount().getId());
                    }
                });
        log.info("Updated credit cards for accountId {}", event.getAccount().getId());

    }

    @Override
    @Transactional
    public void deleteOverdraft(AccountEvent event) {
        repository.findAllByAccountIdAndDeletedOnIsNull(event.getAccount().getId())
                .forEach(it -> {
                    it.setDeletedOn(LocalDateTime.now());
                    log.info("Inactivating Overdraft for accountId {}", event.getAccount().getId());
                });
        log.info("Credit cards for accountId {} inactivated", event.getAccount().getId());
    }
}
