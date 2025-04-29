package org.health.se7a.security.service;

import org.health.se7a.exception.XppException;
import org.health.se7a.security.UserRepository;
import org.health.se7a.security.model.LoginDetails;
import org.health.se7a.security.model.LoginType;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoginDetailsServiceImpl implements LoginDetailsService {
    private final List<UserRepository> loginTypeRepositories;

    @Override
    public UserDetails loadByPhoneNumber(String phoneNumber, LoginType loginType) throws UsernameNotFoundException {
        return getRepositoryByLoginType(loginType)
                .findByTelNumber(phoneNumber)
                .map(temp -> LoginDetails.builder()
                        .id(temp.getId())
                        .username(temp.getName())
                        .accountStatus(temp.getAccountStatus())
                        .phoneNumber(temp.getTelNumber())
                        .build())
                .orElseThrow(() -> new XppException(List.of(phoneNumber), HttpStatus.BAD_REQUEST,"user.not.found"));
    }


    public UserRepository getRepositoryByLoginType(LoginType loginType) throws NotImplementedException {
        return loginTypeRepositories.stream()
                .filter(temp -> loginType.equals(temp.getType()))
                .findFirst()
                .orElseThrow(NotImplementedException::new);
    }


}
