package org.health.se7a.security.filter;


import org.health.se7a.entity.EntityService;
import org.health.se7a.security.model.LoginAuthenticationToken;
import org.health.se7a.security.model.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.health.se7a.security.model.LoginType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;

import static org.health.se7a.security.SecurityConstants.LOGIN_URL;
import static org.health.se7a.util.HttpObjectParser.parseInputStream;

@Slf4j
public class LoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    @Autowired
    private EntityService entityService;

    public LoginAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(LOGIN_URL, authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
        log.info("Login request");
        LoginRequest loginRequest = parseInputStream(request.getInputStream(), LoginRequest.class);
        LoginType loginType = entityService.getLoginType(loginRequest.getPhoneNumber());
        LoginAuthenticationToken authentication = LoginAuthenticationToken.builder()
                .phoneNumber(loginRequest.getPhoneNumber())
                .loginType(loginType)
                .build();
        return getAuthenticationManager().authenticate(authentication);
    }



}
