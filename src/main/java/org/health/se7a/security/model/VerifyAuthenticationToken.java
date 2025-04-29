package org.health.se7a.security.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

import static org.health.se7a.security.SecurityConstants.TEMP_ROLE_CODE;


@Builder
@AllArgsConstructor
public class VerifyAuthenticationToken implements Authentication {
    private String username;
    private Boolean isAuthenticated;

    @Getter
    @Setter
    private String uuid;

    @Getter
    @Setter
    private String code;

    @Override

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(TEMP_ROLE_CODE));
    }

    @Override
    public Object getCredentials() {
        return isAuthenticated;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.isAuthenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return username;
    }
}
