package org.health.se7a.security.provider;

import org.health.se7a.exception.XppAuthenticationException;
import org.health.se7a.security.UserRepository;
import org.health.se7a.security.model.LoginType;
import org.health.se7a.security.model.LoginUser;
import org.health.se7a.security.model.ResendCodeAuthenticationToken;
import org.health.se7a.security.otp.OTPService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.health.se7a.exception.XppAuthenticationException.tooManyRequests;
import static org.health.se7a.security.jwt.JwtUtil.extractLoginIdFromUserId;

@Service
@RequiredArgsConstructor
public class ResendCodeAuthenticationProvider implements AuthenticationProvider {
    private final OTPService otpService;
    private final List<UserRepository> loginTypeRepositories;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        ResendCodeAuthenticationToken authenticationToken = (ResendCodeAuthenticationToken) authentication;
        if (!checkUserCanGenerateCodes(authenticationToken))
            throw tooManyRequests();
        LoginUser user = findRepositoryByType(((ResendCodeAuthenticationToken) authentication).getLoginType())
                .loadById(extractLoginIdFromUserId(authentication.getName()))
                .orElseThrow(() -> new XppAuthenticationException("user.not.found"));
        authenticationToken.setTelNumber(user.getTelNumber());
        return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ResendCodeAuthenticationToken.class.equals(authentication);
    }

    private boolean checkUserCanGenerateCodes(ResendCodeAuthenticationToken authenticationToken) {
        return otpService.countRecentOTPsByUser(authenticationToken.getName()) <= 5;
    }


    private UserRepository findRepositoryByType(LoginType loginType) {
        return loginTypeRepositories.stream()
                .filter(temp -> temp.getType().equals(loginType))
                .findFirst()
                .orElseThrow(NotImplementedException::new);
    }

}
