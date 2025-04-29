package org.health.se7a.security.model;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerifyCodeRequest {
    private String code;
}
