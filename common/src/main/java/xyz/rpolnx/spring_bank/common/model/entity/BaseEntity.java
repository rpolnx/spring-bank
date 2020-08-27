package xyz.rpolnx.spring_bank.common.model.entity;

import lombok.Data;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
public abstract class BaseEntity {
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void perPersist() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void perUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
