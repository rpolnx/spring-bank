package xyz.rpolnx.spring_bank.service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.rpolnx.spring_bank.common.exceptions.NotFoundException;
import xyz.rpolnx.spring_bank.service.external.ScoreCategoryRepository;
import xyz.rpolnx.spring_bank.service.model.entity.ScoreCategory;
import xyz.rpolnx.spring_bank.service.service.ScoreCategoryService;

@RequiredArgsConstructor
@Service
public class ScoreCategoryServiceImpl implements ScoreCategoryService {
    private final ScoreCategoryRepository repository;

    @Override
    public ScoreCategory getActiveCategory(Integer score) {
        return repository.findActiveScoreCategory(score)
                .orElseThrow(() -> new NotFoundException("Category Not Found"));
    }
}
