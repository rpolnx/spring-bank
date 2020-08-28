package xyz.rpolnx.spring_bank.service.model.entity;

import lombok.*;
import xyz.rpolnx.spring_bank.common.model.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "credit_cards")
@AllArgsConstructor
@NoArgsConstructor
@Data
@With
@Builder
public class CreditCard extends BaseEntity {
    @Id
    private String number;
    private Long accountId;
    private String name;
    private String brandName;
    private String securityCode;
    private LocalDate expiration;
    private Double remainingLimit;
    private Long scoreCategoriesId;
    private LocalDateTime deletedOn;
}
