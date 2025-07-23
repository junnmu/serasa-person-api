package com.serasa.personapi.infrastructure.exception.handler;

import com.serasa.personapi.infrastructure.exception.InvalidCepException;
import com.serasa.personapi.infrastructure.exception.PersonNotFoundException;
import com.serasa.personapi.infrastructure.exception.ViaCepBadRequestException;
import com.serasa.personapi.infrastructure.exception.ViaCepIntegrationException;
import com.serasa.personapi.infrastructure.exchange.error.ErrorMessage;
import com.serasa.personapi.infrastructure.exchange.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@ControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex) {
        return buildResponse(BAD_REQUEST, "SER-10001", "Invalid credentials", ex);
    }

    @ExceptionHandler(ViaCepBadRequestException.class)
    public ResponseEntity<ErrorResponse> handleViaCepBadRequestException(ViaCepBadRequestException ex) {
        return buildResponse(BAD_REQUEST, "SER-10002", "ViaCep returning a BAD REQUEST", ex);
    }

    @ExceptionHandler(InvalidCepException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCepExceptionException(InvalidCepException ex) {
        return buildResponse(BAD_REQUEST, "SER-10003", ex.getMessage(), ex);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        var errors = ex.getBindingResult()
            .getFieldErrors()
            .stream().map(error -> new ErrorMessage("SER-10004", error.getField() + " " + error.getDefaultMessage()))
            .toList();

        return logExceptionAndBuildResponse(BAD_REQUEST, new ErrorResponse(errors), ex);
    }

    @ExceptionHandler(PersonNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePersonNotFoundException(PersonNotFoundException ex) {
        return buildResponse(NOT_FOUND, "SER-10007", "Person not found", ex);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception ex) {
        System.out.println(ex.getMessage());
        return buildResponse(INTERNAL_SERVER_ERROR, "SER-20001", "Unexpected error", ex);
    }

    @ExceptionHandler(ViaCepIntegrationException.class)
    public ResponseEntity<ErrorResponse> handleViaCepIntegrationException(ViaCepIntegrationException ex) {
        return buildResponse(INTERNAL_SERVER_ERROR, "SER-20002", "Error integrating with ViaCep", ex);
    }

    private ResponseEntity<ErrorResponse> buildResponse(
        final HttpStatusCode statusCode,
        final String code,
        final String message,
        final Exception ex
    ) {
        var response = new ErrorResponse(List.of(new ErrorMessage(code, message)));
        return logExceptionAndBuildResponse(statusCode, response, ex);
    }

    private ResponseEntity<ErrorResponse> logExceptionAndBuildResponse(
        final HttpStatusCode statusCode,
        final ErrorResponse errorResponse,
        final Exception ex
    ) {
        if (statusCode.is5xxServerError()) {
            log.error("class=DefaultExceptionHandler, method=logExceptionAndBuildResponse, statusCode={}, exception={}, stackTrace={}",
                statusCode.value(),
                ex.getClass().getName(),
                ExceptionUtils.getStackTrace(ex)
            );
        } else {
            log.warn("class=DefaultExceptionHandler, method=logExceptionAndBuildResponse, statusCode={}, exception={}, stackTrace={}",
                statusCode.value(),
                ex.getClass().getSimpleName(),
                ExceptionUtils.getStackTrace(ex)
            );
        }

        return ResponseEntity.status(statusCode).body(errorResponse);
    }
}
