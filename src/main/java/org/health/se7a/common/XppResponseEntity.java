package org.health.se7a.common;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Builder
@Getter
public class XppResponseEntity<T> extends ResponseEntity<T> {
    private T data;
    private String error;
    private HttpStatus status;

    public XppResponseEntity(T data, HttpStatus status) {
        super(data, status);
    }

    public XppResponseEntity(T data, String error, HttpStatus status) {
        super(data, status);
        this.error = error;
    }

    public static <T> XppResponseEntity<T> map(T data) {
        return XppResponseEntity.<T>builder()
                .data(data)
                .status(HttpStatus.OK)
                .build();
    }
    public static <T> XppResponseEntity<T> map(HttpStatus status) {
        return XppResponseEntity.<T>builder()
                .status(status)
                .build();
    }
}
