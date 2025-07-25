package com.serasa.personapi.infrastructure.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class PhoneValidatorTest {

    private PhoneValidator validator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        validator = new PhoneValidator();
        context = mock(ConstraintValidatorContext.class);
    }

    @Test
    void shouldReturnTrueWhenPhoneIsNull() {
        var result = validator.isValid(null, context);
        assertTrue(result);
    }

    @Test
    void shouldReturnTrueWhenPhoneIsBlank() {
        var result = validator.isValid("   ", context);
        assertTrue(result);
    }

    @Test
    void shouldReturnTrueForValidPhoneWithoutNinthDigit() {
        var result = validator.isValid("1198765432", context);
        assertTrue(result);
    }

    @Test
    void shouldReturnTrueForValidPhoneWithNinthDigit() {
        var result = validator.isValid("11998765432", context);
        assertTrue(result);
    }

    @Test
    void shouldReturnTrueForValidPhoneWithSpecialCharacters() {
        var result = validator.isValid("(11) 99876-5432", context);
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseForInvalidPhoneTooShort() {
        var result = validator.isValid("119876543", context);
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseForInvalidPhoneWithLeadingZero() {
        var result = validator.isValid("01998765432", context);
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseForInvalidCharactersInPhone() {
        var result = validator.isValid("11abc65432", context);
        assertFalse(result);
    }
}