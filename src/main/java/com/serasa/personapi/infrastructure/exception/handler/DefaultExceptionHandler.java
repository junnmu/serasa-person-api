package com.serasa.personapi.infrastructure.exception.handler;

import com.serasa.personapi.infrastructure.exception.InvalidCepException;
import com.serasa.personapi.infrastructure.exception.ViaCepBadRequestException;
import com.serasa.personapi.infrastructure.exception.ViaCepIntegrationException;
import com.serasa.personapi.infrastructure.exchange.error.ErrorMessage;
import com.serasa.personapi.infrastructure.exchange.error.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException() {
        return buildResponse(400, "SER-10001", "Invalid Credentials");
    }

    @ExceptionHandler(ViaCepBadRequestException.class)
    public ResponseEntity<ErrorResponse> handleViaCepBadRequestException() {
        return buildResponse(400, "SER-10002", "ViaCep returning a BAD REQUEST");
    }

    @ExceptionHandler(InvalidCepException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCepExceptionException(InvalidCepException ex) {
        return buildResponse(400, "SER-10003", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        var errors = ex.getBindingResult()
            .getFieldErrors()
            .stream().map(error -> new ErrorMessage("SER-10004", error.getField() + " " + error.getDefaultMessage()))
            .collect(Collectors.toList());

        return buildResponse(400, new ErrorResponse(errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception ex) {
        System.out.println(ex.getClass());
        return buildResponse(500, "SER-20001", "Unexpected Error");
    }

    @ExceptionHandler(ViaCepIntegrationException.class)
    public ResponseEntity<ErrorResponse> handleViaCepIntegrationException() {
        return buildResponse(500, "SER-20002", "Error integrating with ViaCep");
    }

    private ResponseEntity<ErrorResponse> buildResponse(final Integer statusCode, final String code, final String message) {
        var response = new ErrorResponse(List.of(new ErrorMessage(code, message)));

        return buildResponse(statusCode, response);
    }

    private ResponseEntity<ErrorResponse> buildResponse(final Integer statusCode, final ErrorResponse errorResponse) {
        return ResponseEntity.status(statusCode).body(errorResponse);
    }
}
