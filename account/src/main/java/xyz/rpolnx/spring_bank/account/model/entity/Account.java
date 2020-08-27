package xyz.rpolnx.spring_bank.account.model.entity;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import xyz.rpolnx.spring_bank.account.model.enums.AccountStatus;
import xyz.rpolnx.spring_bank.account.model.enums.AccountType;
import xyz.rpolnx.spring_bank.common.model.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.validation.constraints.Max;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "accounts")
@AllArgsConstructor
@NoArgsConstructor
@Data
@With
public class Account extends BaseEntity {
    @Id
    private Long number;
    @Max(14)
    private String clientId;
    @Length(min = 11, max = 14)
    private String agency;
    @Enumerated(EnumType.STRING)
    private AccountType type;
    private AccountStatus status;
}