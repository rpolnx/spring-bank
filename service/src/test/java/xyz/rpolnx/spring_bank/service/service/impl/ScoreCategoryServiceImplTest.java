package xyz.rpolnx.spring_bank.service.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import xyz.rpolnx.spring_bank.common.exceptions.NotFoundException;
import xyz.rpolnx.spring_bank.service.external.ScoreCategoryRepository;
import xyz.rpolnx.spring_bank.service.model.entity.ScoreCategory;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ScoreCategoryServiceImplTest {
    @Mock
    private ScoreCategoryRepository repository;

    @InjectMocks
    private ScoreCategoryServiceImpl service;

    @Test
    @DisplayName("Should return category")
    public void getCategory() {
        int score = 2;
        ScoreCategory scoreCategory = new ScoreCategory();

        Mockito.when(repository.findActiveScoreCategory(score)).thenReturn(Optional.of(scoreCategory));

        ScoreCategory activeCategory = service.getActiveCategory(score);

        assertEquals(scoreCategory, activeCategory);
    }

    @Test
    @DisplayName("Should throw exception when category not found")
    public void shouldThrowError() {
        int score = 2;
        ScoreCategory scoreCategory = new ScoreCategory();

        Mockito.when(repository.findActiveScoreCategory(score)).thenReturn(Optional.empty());

        NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> service.getActiveCategory(score));

        assertTrue(notFoundException.getMessage().contains("Category Not Found"));
    }

}