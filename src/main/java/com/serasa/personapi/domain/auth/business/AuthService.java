package com.serasa.personapi.domain.auth.business;

import com.serasa.personapi.infrastructure.exchange.request.AuthRequest;
import com.serasa.personapi.infrastructure.exchange.response.AuthResponse;
import com.serasa.personapi.infrastructure.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthResponse authenticate(AuthRequest request) {
        var authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        var token = jwtUtil.generateToken(authentication.getName());
        return new AuthResponse(token);
    }
}
