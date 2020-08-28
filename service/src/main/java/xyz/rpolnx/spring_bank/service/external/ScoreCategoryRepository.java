package xyz.rpolnx.spring_bank.service.external;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import xyz.rpolnx.spring_bank.service.model.entity.ScoreCategory;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScoreCategoryRepository extends CrudRepository<ScoreCategory, Long> {
    @Override
    List<ScoreCategory> findAll();

    @Query("SELECT sc FROM score_categories sc WHERE sc.lowerScoreLimit <= ?1 AND sc.higherScoreLimit >= ?1 AND sc.deletedOn IS NULL")
    Optional<ScoreCategory> findActiveScoreCategory(Integer score);
}
