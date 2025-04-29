package org.health.se7a.common;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
public class XppResponse<T> {
    private HttpStatus status;
    private T content;
    private String message;

    public static <T> XppResponse<T> map(final T data, final HttpStatus status) {
        return XppResponse.<T>builder()
                .content(data)
                .status(status)
                .build();
    }

    public static <T> XppResponse<T> map(final HttpStatus status) {
        return XppResponse.<T>builder()
                .status(status)
                .build();
    }
    public static <T> XppResponse<T> map(T content, HttpStatus status, String message) {
        return XppResponse.<T>builder()
                .content(content)
                .status(status)
                .message(message)
                .build();
    }


}
