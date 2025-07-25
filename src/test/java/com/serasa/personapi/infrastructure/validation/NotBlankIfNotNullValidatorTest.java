package com.serasa.personapi.infrastructure.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class NotBlankIfNotNullValidatorTest {

    private NotBlankIfNotNullValidator validator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        validator = new NotBlankIfNotNullValidator();
        context = mock(ConstraintValidatorContext.class);
    }

    @Test
    void shouldReturnTrueWhenValueIsNull() {
        var result = validator.isValid(null, context);
        assertTrue(result);
    }

    @Test
    void shouldReturnTrueWhenValueIsNotBlank() {
        var result = validator.isValid("valid", context);
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenValueIsBlank() {
        var result = validator.isValid("   ", context);
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenValueIsEmpty() {
        var result = validator.isValid("", context);
        assertFalse(result);
    }
}