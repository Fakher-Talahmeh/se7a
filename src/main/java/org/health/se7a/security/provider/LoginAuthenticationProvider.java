package org.health.se7a.security.provider;

import org.health.se7a.exception.XppAuthenticationException;
import org.health.se7a.security.model.AccountStatus;
import org.health.se7a.security.model.LoginAuthenticationToken;
import org.health.se7a.security.model.LoginDetails;
import org.health.se7a.security.service.LoginDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginAuthenticationProvider implements AuthenticationProvider {

    private final LoginDetailsService loginDetailsService;
    private final MessageSource messageSource;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            LoginAuthenticationToken usernameAndPasswordAuthentication = ((LoginAuthenticationToken) authentication);
            LoginDetails userDetails = (LoginDetails) loginDetailsService.loadByPhoneNumber(usernameAndPasswordAuthentication.getPhoneNumber(),
                    usernameAndPasswordAuthentication.getLoginType());
            if(userDetails.getAccountStatus() == AccountStatus.DISABLED) {
                throw new XppAuthenticationException("user.account.disabled");
            }
            usernameAndPasswordAuthentication.setUsername(userDetails.getUsername());
            usernameAndPasswordAuthentication.setId(userDetails.getId());
            return usernameAndPasswordAuthentication;
        } catch (Exception e) {
            throw new BadCredentialsException("login.error.process");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return LoginAuthenticationToken.class.equals(authentication);
    }

}
