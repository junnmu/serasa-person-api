package com.serasa.personapi.infrastructure.exchange.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {
    private String username;
    private String password;

    @Override
    public String toString() {
        return "AuthRequest(username=" + username + ", password=*****)";
    }
}
