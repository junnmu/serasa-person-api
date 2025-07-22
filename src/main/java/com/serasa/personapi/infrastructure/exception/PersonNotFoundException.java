package com.serasa.personapi.infrastructure.exception;

public class PersonNotFoundException extends RuntimeException {

    public PersonNotFoundException(String message) {
        super(message);
    }
}
