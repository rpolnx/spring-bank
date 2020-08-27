package xyz.rpolnx.spring_bank.account.model.entity;

import lombok.*;
import org.springframework.data.domain.Persistable;
import xyz.rpolnx.spring_bank.account.model.enums.AccountStatus;
import xyz.rpolnx.spring_bank.account.model.enums.AccountType;
import xyz.rpolnx.spring_bank.common.model.entity.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Size;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "accounts")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@With
public class Account extends BaseEntity implements Persistable<Long> {
    @Id
    @Max(999999)
    private Long number;

    @Size(min = 11, max = 14)
    private String clientId;

    @Size(max = 6)
    private String agency;

    @Enumerated(EnumType.STRING)
    private AccountType type;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @Transient
    private boolean update;

    @Override
    public Long getId() {
        return this.number;
    }

    @Override
    public boolean isNew() {
        return !this.update;
    }

    @PrePersist
    @PostLoad
    void markUpdated() {
        this.update = true;
    }
}
