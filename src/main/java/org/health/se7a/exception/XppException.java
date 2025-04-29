package org.health.se7a.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Builder
@Getter
public class XppException extends RuntimeException {


    private List<Object> params;

    private HttpStatus status;

    private String message;

    public XppException(List<Object> params, HttpStatus status, String message) {
        super(message);
        this.params = params;
        this.status = status;
        this.message = message;
    }

    public XppException(String message) {
        this.message = message;
    }

    public static XppException withMessage(String message) {
        return XppException.builder()
                .message(message)
                .build();
    }
}
