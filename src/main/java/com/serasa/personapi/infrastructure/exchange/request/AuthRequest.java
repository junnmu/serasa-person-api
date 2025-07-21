package com.serasa.personapi.infrastructure.exchange.request;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}
