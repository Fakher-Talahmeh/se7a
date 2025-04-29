package org.health.se7a.security.handler.verify;

import org.health.se7a.security.jwt.JwtGenerator;
import org.health.se7a.security.jwt.JwtRequest;
import org.health.se7a.security.model.VerifyAuthenticationToken;
import org.health.se7a.security.otp.OTPService;
import org.health.se7a.security.otp.OTPStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import static org.health.se7a.security.SecurityConstants.ACCESS_TOKEN_HEADER_NAME;
import static org.health.se7a.util.DateUtil.convertDaysToMs;

@Service
@RequiredArgsConstructor
@Slf4j
public class VerifyCodeSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtGenerator jwtGenerator;
    private final OTPService otpService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        VerifyAuthenticationToken verifyAuthenticationToken = (VerifyAuthenticationToken) authentication;
        verifyAuthenticationToken.setAuthenticated(true);
        otpService.setOtpStatus(verifyAuthenticationToken.getUuid(), OTPStatus.VERIFIED);
        generateFinalJwtToken(verifyAuthenticationToken, response);
    }

    private void generateFinalJwtToken(Authentication authentication, HttpServletResponse response) {
        JwtRequest request = JwtRequest.builder()
                .subject(authentication.getName())
                .expiresAfterMin(convertDaysToMs(14L))
                .build();
        String jwt = jwtGenerator.generateJwtToken(request);
        response.setHeader(ACCESS_TOKEN_HEADER_NAME, jwt);
    }
}
