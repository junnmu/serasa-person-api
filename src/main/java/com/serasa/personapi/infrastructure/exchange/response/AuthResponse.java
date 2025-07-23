package com.serasa.personapi.infrastructure.exchange.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;

    @Override
    public String toString() {
        return "AuthResponse(token=*****)";
    }
}
