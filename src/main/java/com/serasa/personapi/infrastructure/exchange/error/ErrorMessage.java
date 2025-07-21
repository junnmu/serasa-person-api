package com.serasa.personapi.infrastructure.exchange.error;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorMessage {
    private String code;
    private String message;
}
