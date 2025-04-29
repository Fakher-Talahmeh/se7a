package org.health.se7a.security.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class LoginDetails implements UserDetails {
    private String username;
    private String phoneNumber;
    private LoginType loginType;
    private AccountStatus accountStatus;

    @Getter
    @Setter
    private Long id;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(loginType.name()));
    }

    @Override
    public String getPassword() {
        return null;
    }



}
