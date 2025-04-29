package org.health.se7a.entity;

import jakarta.persistence.*;
import lombok.*;
import org.health.se7a.security.model.LoginType;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String telNumber;

    @Enumerated(EnumType.STRING)
    private LoginType loginType ;
}