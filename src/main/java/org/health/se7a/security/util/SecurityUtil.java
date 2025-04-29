package org.health.se7a.security.util;

import org.health.se7a.common.XppResponse;
import org.health.se7a.security.model.LoginType;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.health.se7a.security.handler.login.LoginAuthenticationFailureHandler.APPLICATION_TYPE_JSON_UTF8;
import static org.health.se7a.util.HttpObjectParser.toJson;
import static org.apache.commons.lang3.ObjectUtils.firstNonNull;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RequiredArgsConstructor
@Component
public class SecurityUtil {
    private final MessageSource messageSource;


    public static String createSecurityId(LoginType type, Long id) {
        return type.toString()
                .concat(":")
                .concat(id + "");
    }

    public  XppResponse toErrorResponse(AuthenticationException exception) {
        return XppResponse.builder()
                .content(firstNonNull(getMessage(exception.getMessage()),"Run time"))
                .status(UNAUTHORIZED)
                .build();
    }

    public  void writeResponse(HttpServletResponse response, AuthenticationException e) {
        try {
            response.setContentType(APPLICATION_TYPE_JSON_UTF8);
            response.setStatus(UNAUTHORIZED.value());
            response.getWriter().write(toJson(toErrorResponse(e)));
        } catch (IOException ex) {
        }
    }

    public String getMessage(String code) {
        return messageSource.getMessage(code,null, LocaleContextHolder.getLocale());
    }
}
