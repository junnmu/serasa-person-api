package com.serasa.personapi.infrastructure.exchange.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private List<ErrorMessage> errorMessages;
}
