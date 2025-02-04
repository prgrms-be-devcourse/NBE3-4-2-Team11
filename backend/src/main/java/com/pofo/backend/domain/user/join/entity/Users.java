package com.pofo.backend.domain.user.join.entity;

import com.pofo.backend.common.jpa.entity.BaseEntity;
import com.pofo.backend.common.jpa.entity.BaseTime;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users",
        uniqueConstraints = { @UniqueConstraint(columnNames = "email") })
public class Users extends BaseEntity {

    @Column(unique = true)
    @NotNull(message = "email 값이 필요합니다.")
    public String email;

    @NotNull(message = "name 값이 필요합니다.")
    public String name;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "sex 값이 필요합니다.")
    public Sex sex;

    @NotNull(message = "nickname 값이 필요합니다.")
    public String nickname;

    @NotNull(message = "age 값이 필요합니다.")
    public LocalDate age;

    @CreatedDate
    @Setter(AccessLevel.PRIVATE)
    private LocalDateTime createdAt;

    @Getter
    @AllArgsConstructor
    public enum Sex {
        MALE,
        FEMALE;

    }
}
