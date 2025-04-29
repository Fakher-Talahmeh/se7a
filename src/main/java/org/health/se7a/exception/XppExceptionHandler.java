package org.health.se7a.exception;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.health.se7a.common.XppResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class XppExceptionHandler {

    private final MessageSource messageSource;


    @ExceptionHandler(XppException.class)
    public ResponseEntity<XppResponse<Object>> handleXppException(XppException exception) {
        log.error("Xpp exception occurred: {}", exception.getMessage());
        XppResponse<Object> response = XppResponse.map(
                null,
                exception.getStatus(),
                messageSource.getMessage(exception.getMessage(),
                        new Object[]{exception.getParams()},
                        LocaleContextHolder.getLocale())
        );

        return new ResponseEntity<>(response, exception.getStatus());
    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<XppResponse<Object>> handleRuntimeException(RuntimeException exception) {
        log.info("Runtime exception");
        XppResponse<Object> response = XppResponse.map(
                null,
                HttpStatus.INTERNAL_SERVER_ERROR,
                messageSource.getMessage(exception.getMessage(),
                        new Object[]{},
                        LocaleContextHolder.getLocale())
        );

        return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR );
    }

    @ExceptionHandler(XppAuthenticationException.class)
    public ResponseEntity<XppResponse<Object>> handleXppAuthenticationException(XppAuthenticationException exception) {
        log.info("Authentication exception");
        XppResponse<Object> response = XppResponse.map(
                null,
                HttpStatus.UNAUTHORIZED,
                messageSource.getMessage(exception.getMessage(),
                        new Object[]{},
                        LocaleContextHolder.getLocale())
        );

        return new ResponseEntity<>(response,HttpStatus.UNAUTHORIZED );    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<XppResponse<Object>> handleAccessDeniedException(AccessDeniedException exception) {
        log.info("Denied exception");
        XppResponse<Object> response = XppResponse.map(
                null,
                HttpStatus.FORBIDDEN,
                messageSource.getMessage("access.denied",
                        new Object[]{},
                        LocaleContextHolder.getLocale())
        );

        return new ResponseEntity<>(response,HttpStatus.FORBIDDEN );    }


    @ExceptionHandler({ MaxUploadSizeExceededException.class, IllegalStateException.class })
    public ResponseEntity<XppResponse<Object>> handleUploadExceptions(Exception ex) {
        String errorMessage = messageSource.getMessage(
                "exception.upload.too-large",
                null,
                LocaleContextHolder.getLocale()
        );

        XppResponse<Object> response = XppResponse.map(null, HttpStatus.PAYLOAD_TOO_LARGE, errorMessage);

        return new ResponseEntity<>(response, HttpStatus.PAYLOAD_TOO_LARGE);
    }



    @ExceptionHandler(UnrecognizedPropertyException.class)
    public ResponseEntity<XppResponse<Object>> handleException(UnrecognizedPropertyException exception) {
        log.info("UnrecognizedProperty exception");
        XppResponse<Object> response = XppResponse.map(
                null,
                HttpStatus.INTERNAL_SERVER_ERROR,
                messageSource.getMessage(exception.getMessage(),
                        new Object[]{},
                        LocaleContextHolder.getLocale())
        );

        return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR );    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<XppResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        XppResponse<Map<String, String>> response = XppResponse.<Map<String, String>>builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("Invalid user data")
                .content(errors)
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }




    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<XppResponse<Object>> handleMissingRequestBody(HttpMessageNotReadableException ex) {
        String messageKey = "invalid.request.format";
        String message = messageSource.getMessage(messageKey, null, messageKey, LocaleContextHolder.getLocale());
        XppResponse<Object> response = XppResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(message)
                .content(null)
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
