package org.health.se7a.security.handler.resend;

import org.health.se7a.security.util.SecurityUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
@RequiredArgsConstructor
public class ResendCodeFailureHandler implements AuthenticationFailureHandler {
    private final SecurityUtil securityUtil;
    private static final Logger log = LoggerFactory.getLogger(ResendCodeFailureHandler.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.warn("Error during login process: {}", exception.getMessage());
        securityUtil.writeResponse(response, exception);
    }
}
