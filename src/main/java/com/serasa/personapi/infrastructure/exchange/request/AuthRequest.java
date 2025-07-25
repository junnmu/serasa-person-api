package com.serasa.personapi.infrastructure.exchange.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthRequest {
    private String username;
    private String password;

    @Override
    public String toString() {
        return "AuthRequest(username=" + username + ", password=*****)";
    }
}
