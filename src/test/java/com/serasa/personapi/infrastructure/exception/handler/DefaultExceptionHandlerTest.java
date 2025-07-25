package com.serasa.personapi.infrastructure.exception.handler;

import com.serasa.personapi.infrastructure.exception.InvalidCepException;
import com.serasa.personapi.infrastructure.exception.PersonNotFoundException;
import com.serasa.personapi.infrastructure.exception.ViaCepBadRequestException;
import com.serasa.personapi.infrastructure.exception.ViaCepIntegrationException;
import com.serasa.personapi.infrastructure.exchange.error.ErrorCode;
import com.serasa.personapi.infrastructure.exchange.error.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.ModelAttributeMethodProcessor;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultExceptionHandlerTest {

    private DefaultExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new DefaultExceptionHandler();
    }

    @Test
    void shouldHandleBadCredentialsException() {
        var ex = new BadCredentialsException("Invalid credentials");

        ResponseEntity<ErrorResponse> response = handler.handleBadCredentialsException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        var errors = response.getBody().getErrorMessages();
        assertEquals(1, errors.size());
        assertEquals(ErrorCode.INVALID_CREDENTIALS.getCode(), errors.get(0).getCode());
    }

    @Test
    void shouldHandleViaCepBadRequestException() {
        var ex = new ViaCepBadRequestException("Invalid CEP");

        ResponseEntity<ErrorResponse> response = handler.handleViaCepBadRequestException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        var errors = response.getBody().getErrorMessages();
        assertEquals(1, errors.size());
        assertEquals(ErrorCode.VIACEP_BAD_REQUEST.getCode(), errors.get(0).getCode());
    }

    @Test
    void shouldHandleInvalidCepException() {
        var ex = new InvalidCepException("Invalid");

        ResponseEntity<ErrorResponse> response = handler.handleInvalidCepException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        var errors = response.getBody().getErrorMessages();
        assertEquals(1, errors.size());
        assertEquals(ErrorCode.INVALID_CEP.getCode(), errors.get(0).getCode());
    }

    @Test
    void shouldHandlePersonNotFoundException() {
        var ex = new PersonNotFoundException("Not found");

        ResponseEntity<ErrorResponse> response = handler.handlePersonNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        var errors = response.getBody().getErrorMessages();
        assertEquals(1, errors.size());
        assertEquals(ErrorCode.PERSON_NOT_FOUND.getCode(), errors.get(0).getCode());
    }

    @Test
    void shouldHandleMethodArgumentNotValidException() throws NoSuchMethodException {
        var bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(
            List.of(new FieldError("person", "name", "must not be blank"))
        );

        Method method = ModelAttributeMethodProcessor.class.getMethod("supportsParameter", MethodParameter.class);
        MethodParameter methodParameter = new MethodParameter(method, 0);

        var ex = new MethodArgumentNotValidException(methodParameter, bindingResult);

        var response = handler.handleMethodArgumentNotValidException(ex);

        assertEquals(400, response.getStatusCode().value());
        var errors = response.getBody().getErrorMessages();
        assertEquals(1, errors.size());
        assertEquals(ErrorCode.FIELD_VALIDATION_ERROR.getCode(), errors.get(0).getCode());
        assertEquals("name must not be blank", errors.get(0).getMessage());
    }

    @Test
    void shouldHandleViaCepIntegrationException() {
        var cause = new RuntimeException("Feign error");
        var ex = new ViaCepIntegrationException(cause);

        ResponseEntity<ErrorResponse> response = handler.handleViaCepIntegrationException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        var errors = response.getBody().getErrorMessages();
        assertEquals(1, errors.size());
        assertEquals(ErrorCode.VIACEP_INTEGRATION_ERROR.getCode(), errors.get(0).getCode());
    }

    @Test
    void shouldHandleUnexpectedException() throws Exception {
        var ex = new RuntimeException("Unexpected");

        ResponseEntity<ErrorResponse> response = handler.handleUnexpectedException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        var errors = response.getBody().getErrorMessages();
        assertEquals(1, errors.size());
        assertEquals(ErrorCode.UNEXPECTED_ERROR.getCode(), errors.get(0).getCode());
    }

    @Test
    void shouldRethrowAccessDeniedException() {
        var ex = new AccessDeniedException("Access denied");

        assertThrows(AccessDeniedException.class, () -> handler.handleUnexpectedException(ex));
    }
}