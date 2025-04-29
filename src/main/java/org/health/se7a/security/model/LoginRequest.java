package org.health.se7a.security.model;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    private String phoneNumber;
    private LoginType type;
}
