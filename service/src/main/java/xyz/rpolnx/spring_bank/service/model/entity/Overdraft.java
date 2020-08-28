package xyz.rpolnx.spring_bank.service.model.entity;

import lombok.*;
import xyz.rpolnx.spring_bank.common.model.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.EAGER;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "overdrafts")
@AllArgsConstructor
@NoArgsConstructor
@Data
@With
@Builder
public class Overdraft extends BaseEntity {
    @Id
    private Long accountId;
    private Double remainingLimit;

    @ManyToOne(fetch = EAGER)
    private ScoreCategory scoreCategory;
    private LocalDateTime deletedOn;
}
