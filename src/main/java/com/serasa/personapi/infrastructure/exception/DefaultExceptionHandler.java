package com.serasa.personapi.infrastructure.exception;

import com.serasa.personapi.infrastructure.exchange.error.ErrorMessage;
import com.serasa.personapi.infrastructure.exchange.error.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException() {
        return buildResponse(400, "SER-10001", "Invalid Credentials");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException() {
        return buildResponse(500, "SER-20001", "Unexpected Error");
    }

    private ResponseEntity<ErrorResponse> buildResponse(final Integer statusCode, final String code, final String message) {
        var response = new ErrorResponse(List.of(new ErrorMessage(code, message)));

        return buildResponse(statusCode, response);
    }

    private ResponseEntity<ErrorResponse> buildResponse(final Integer statusCode, final ErrorResponse errorResponse) {
        return ResponseEntity.status(statusCode).body(errorResponse);
    }
}
