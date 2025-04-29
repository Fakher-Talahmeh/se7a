package org.health.se7a.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.AuthenticationException;


@Getter
@Setter
@Builder
public class XppAuthenticationException extends AuthenticationException {
    private String message;

    public XppAuthenticationException(String msg) {
        super(msg);
        this.message = msg;
    }

    public static XppAuthenticationException wrongCode() {
        return XppAuthenticationException.builder()
                .message("authentication.wrongCode")
                .build();
    }

    public static XppAuthenticationException invalidToken() {
        return XppAuthenticationException.builder()
                .message("authentication.invalidToken")
                .build();
    }

    public static XppAuthenticationException tooManyVerifications() {
        return XppAuthenticationException.builder()
                .message("authentication.tooManyVerifications")
                .build();
    }

    public static XppAuthenticationException tooManyRequests(){
        return XppAuthenticationException.builder()
                .message("authentication.tooManyRequests")
                .build();
    }

}
