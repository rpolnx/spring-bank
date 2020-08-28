package xyz.rpolnx.spring_bank.service.model.entity;

import lombok.*;
import xyz.rpolnx.spring_bank.common.model.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "overdrafts")
@AllArgsConstructor
@NoArgsConstructor
@Data
@With
public class Overdraft extends BaseEntity {
    @Id
    private Long accountId;
    private Double remainingLimit;
    private Long scoreCategoriesId;
    private LocalDateTime deletedOn;
}
