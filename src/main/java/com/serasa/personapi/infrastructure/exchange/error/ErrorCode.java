package com.serasa.personapi.infrastructure.exchange.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INVALID_CREDENTIALS      ("SER-10001", "Invalid credentials"),
    VIACEP_BAD_REQUEST       ("SER-10002", "ViaCep returning a BAD REQUEST"),
    INVALID_CEP              ("SER-10003", "Invalid CEP"),
    FIELD_VALIDATION_ERROR   ("SER-10004", "Field validation error"),
    ACCESS_DENIED            ("SER-10005", "You do not have permission to access this resource"),
    INVALID_TOKEN            ("SER-10006", "Invalid or expired token"),
    PERSON_NOT_FOUND         ("SER-10007", "Person not found"),

    UNEXPECTED_ERROR         ("SER-20001", "Unexpected error"),
    VIACEP_INTEGRATION_ERROR ("SER-20002", "Error integrating with ViaCep");

    private final String code;
    private final String message;
}
