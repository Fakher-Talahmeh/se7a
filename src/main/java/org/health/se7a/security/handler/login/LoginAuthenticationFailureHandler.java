package org.health.se7a.security.handler.login;

import org.health.se7a.security.util.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;


@Component
@RequiredArgsConstructor
@Slf4j
public class LoginAuthenticationFailureHandler implements AuthenticationFailureHandler {

    public static final String APPLICATION_TYPE_JSON_UTF8 = "application/json;charset=UTF-8";
    private final SecurityUtil securityUtil;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
        log.warn("Error during login process: {}", exception.getMessage());
        securityUtil.writeResponse(response, exception);
    }

}
