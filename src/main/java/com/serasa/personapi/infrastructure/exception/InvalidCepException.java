package com.serasa.personapi.infrastructure.exception;

public class InvalidCepException extends RuntimeException {

    public InvalidCepException(String message) {
        super(message);
    }
}
