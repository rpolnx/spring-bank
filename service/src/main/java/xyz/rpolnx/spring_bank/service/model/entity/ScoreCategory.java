package xyz.rpolnx.spring_bank.service.model.entity;

import lombok.*;
import xyz.rpolnx.spring_bank.common.model.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "score_categories")
@AllArgsConstructor
@NoArgsConstructor
@Data
@With
public class ScoreCategory extends BaseEntity {
    @Id
    private Long id;
    @NotNull
    private Integer lowerScoreLimit;
    @NotNull
    private Integer higherScoreLimit;
    private Double creditCardLimit;
    private Double overdraftLimit;
    private LocalDateTime deletedOn;
}
