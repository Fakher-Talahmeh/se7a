package org.health.se7a.security.jwt;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtRequest {
    private String subject;
    private Long expiresAfterMin;
    private Map<String, Object> claims;


    public JwtRequest appendClaim(String key, Object value) {
        if (isNull(claims))
            this.claims = new HashMap<>();
        this.claims.put(key, value);
        return this;
    }

    public JwtRequest setRole(String role) {
        if (isNull(claims))
            this.claims = new HashMap<>();
        this.claims.put(role, true);
        return this;
    }
}
