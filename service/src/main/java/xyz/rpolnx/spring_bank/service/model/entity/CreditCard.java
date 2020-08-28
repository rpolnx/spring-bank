package xyz.rpolnx.spring_bank.service.model.entity;

import lombok.*;
import xyz.rpolnx.spring_bank.common.model.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.EAGER;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "credit_cards")
@AllArgsConstructor
@NoArgsConstructor
@Data
@With
public class CreditCard extends BaseEntity {
    @Id
    private String number;
    private Long accountId;
    private String name;
    private String brandName;
    private String securityCode;
    private LocalDate expiration;
    private Double remainingLimit;

    @ManyToOne(fetch = EAGER)
    private ScoreCategory scoreCategory;
    private LocalDateTime deletedOn;
}
