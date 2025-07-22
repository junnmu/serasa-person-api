package com.serasa.personapi.infrastructure.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {

    private static final String PHONE_REGEX = "^[1-9]{2}9?[0-9]{4}[0-9]{4}$";

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        if (phone == null || phone.isBlank()) {
            return true;
        }
        return phone.replaceAll("[^\\d]", "").matches(PHONE_REGEX);
    }
}
