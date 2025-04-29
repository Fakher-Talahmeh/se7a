package org.health.se7a.security.handler.verify;

import org.health.se7a.security.util.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Service
@RequiredArgsConstructor
@Slf4j
public class VerifyCodeFailureHandler implements AuthenticationFailureHandler {
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final SecurityUtil securityUtil;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
        log.warn("Error during verification process: {}", exception.getMessage());
        securityUtil.writeResponse(response, exception);
    }

}
