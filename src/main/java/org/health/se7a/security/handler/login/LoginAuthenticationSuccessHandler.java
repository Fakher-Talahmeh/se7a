package org.health.se7a.security.handler.login;

import org.health.se7a.exception.XppAuthenticationException;
import org.health.se7a.notifications.MessageProvider;
import org.health.se7a.notifications.MessageRequest;
import org.health.se7a.notifications.MessageResponse;
import org.health.se7a.security.jwt.JwtGenerator;
import org.health.se7a.security.jwt.JwtRequest;
import org.health.se7a.security.model.LoginAuthenticationToken;
import org.health.se7a.security.otp.OTPService;
import org.health.se7a.security.util.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import static org.health.se7a.exception.XppAuthenticationException.tooManyRequests;
import static org.health.se7a.notifications.MessageStatus.DELIVERED;
import static org.health.se7a.security.SecurityConstants.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtGenerator jwtGenerator;
    private final MessageProvider messageProvider;
    private final OTPService otpService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        LoginAuthenticationToken loginAuthenticationToken = (LoginAuthenticationToken) authentication;
        loginAuthenticationToken.setAuthenticated(true);
        MessageRequest otpRequest = new MessageRequest(loginAuthenticationToken.getPhoneNumber(), "0000");
        MessageResponse otpResponse = messageProvider.sendMessage(otpRequest);
        if (isMessageDelivered(otpResponse))
            successOtpDelivery(response, otpRequest, otpResponse, loginAuthenticationToken);
        else failureOtpDelivery();
    }


    private Boolean isMessageDelivered(MessageResponse response) {
        return DELIVERED.equals(response.getStatus());
    }

    private void createOtp(LoginAuthenticationToken authenticationToken, MessageRequest request, MessageResponse response) {
        if (checkIfUserCanSendOtp(authenticationToken))
            otpService.create(SecurityUtil.createSecurityId(authenticationToken.getLoginType(), authenticationToken.getId()), request.getBody(), response.getUuid());
        else throw tooManyRequests();
    }

    private JwtRequest buildJwtRequest(LoginAuthenticationToken authenticationToken, MessageResponse response) {
        return JwtRequest.builder()
                .subject(SecurityUtil.createSecurityId(authenticationToken.getLoginType(), authenticationToken.getId()))
                .expiresAfterMin(10L)
                .build()
                .appendClaim(UUID_KEY_NAME, response.getUuid())
                .setRole(TEMP_ROLE_CODE);
    }

    private Boolean checkIfUserCanSendOtp(LoginAuthenticationToken token) {
        return otpService.countRecentOTPsByUser(SecurityUtil.createSecurityId(token.getLoginType(), token.getId())) <= 5;
    }


    private void successOtpDelivery(HttpServletResponse response, MessageRequest messageRequest, MessageResponse messageResponse, LoginAuthenticationToken loginAuthenticationToken) {
        createOtp(loginAuthenticationToken, messageRequest, messageResponse);
        JwtRequest jwtRequest = buildJwtRequest(loginAuthenticationToken, messageResponse);
        String token = jwtGenerator.generateJwtToken(jwtRequest);
        response.setHeader(ACCESS_TOKEN_HEADER_NAME, token);
    }

    private static void failureOtpDelivery() {
        throw XppAuthenticationException.builder()
                .message("Message couldn't be delivered")
                .build();
    }

}
