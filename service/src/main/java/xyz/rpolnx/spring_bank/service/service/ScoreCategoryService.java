package xyz.rpolnx.spring_bank.service.service;

import xyz.rpolnx.spring_bank.service.model.entity.ScoreCategory;

public interface ScoreCategoryService {

    ScoreCategory getActiveCategory(Integer score);
}
