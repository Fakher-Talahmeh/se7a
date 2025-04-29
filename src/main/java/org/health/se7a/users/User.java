package org.health.se7a.users;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.health.se7a.common.IDGenerator;
import org.health.se7a.security.model.AccountStatus;
import org.health.se7a.security.model.LoginType;
import org.health.se7a.security.model.LoginUser;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User  {


    @Id
    @GeneratedValue(generator = "IDGenerator")
    @GenericGenerator(name = "IDGenerator", strategy = "org.health.se7a.common.IDGenerator")
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    private String name;

    private String telNumber;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus ;


    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @LastModifiedDate
    @Column(name = "updated_at",insertable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdBy = getUserName();
    }

    private String getUserName() {
        return createdBy;

    }
}
