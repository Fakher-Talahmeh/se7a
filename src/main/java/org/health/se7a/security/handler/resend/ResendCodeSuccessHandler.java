package org.health.se7a.security.handler.resend;

import org.health.se7a.notifications.MessageProvider;
import org.health.se7a.notifications.MessageRequest;
import org.health.se7a.notifications.MessageResponse;
import org.health.se7a.security.jwt.JwtGenerator;
import org.health.se7a.security.jwt.JwtRequest;
import org.health.se7a.security.model.ResendCodeAuthenticationToken;
import org.health.se7a.security.otp.OTPService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.health.se7a.notifications.MessageStatus.DELIVERED;
import static org.health.se7a.security.SecurityConstants.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class ResendCodeSuccessHandler implements AuthenticationSuccessHandler {
    private final MessageProvider messageProvider;
    private final JwtGenerator jwtGenerator;
    private final OTPService otpService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        ResendCodeAuthenticationToken token = (ResendCodeAuthenticationToken) authentication;
        authentication.setAuthenticated(true);
        MessageRequest messageRequest = MessageRequest.builder()
                .body("1111")
                .destination(token.getTelNumber())
                .build();
        MessageResponse messageResponse = messageProvider.sendMessage(messageRequest);
        if (isDeliverySuccess(messageResponse))
            successOtpDelivery(response, authentication, messageRequest, messageResponse, token);
    }

    private boolean isDeliverySuccess(MessageResponse messageResponse) {
        return DELIVERED.equals(messageResponse.getStatus());
    }

    private void successOtpDelivery(HttpServletResponse response, Authentication authentication, MessageRequest messageRequest, MessageResponse messageResponse, ResendCodeAuthenticationToken token) {
        otpService.create(authentication.getName(), messageRequest.getBody(), messageResponse.getUuid());
        JwtRequest jwtRequest = JwtRequest.builder()
                .subject(token.getName())
                .expiresAfterMin(10L)
                .build()
                .setRole(TEMP_ROLE_CODE)
                .appendClaim(UUID_KEY_NAME, messageResponse.getUuid());
        String accessToken = jwtGenerator.generateJwtToken(jwtRequest);
        response.setHeader(ACCESS_TOKEN_HEADER_NAME, accessToken);
    }
}
