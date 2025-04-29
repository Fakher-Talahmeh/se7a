package org.health.se7a.security.service;

import org.health.se7a.security.model.LoginType;
import jakarta.transaction.NotSupportedException;
import lombok.SneakyThrows;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface LoginDetailsService extends UserDetailsService {
    @SneakyThrows
    @Override
    default UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        throw new NotSupportedException();
    }

    UserDetails loadByPhoneNumber(String phoneNumber, LoginType loginType);
}
