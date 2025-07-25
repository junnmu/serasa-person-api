package com.serasa.personapi.domain.auth.business;

import com.serasa.personapi.infrastructure.exchange.request.AuthRequest;
import com.serasa.personapi.infrastructure.exchange.response.AuthResponse;
import com.serasa.personapi.infrastructure.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class AuthServiceTest {

    private AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        authenticationManager = mock(AuthenticationManager.class);
        jwtUtil = mock(JwtUtil.class);
        authService = new AuthService(authenticationManager, jwtUtil);
    }

    @Test
    void shouldAuthenticateSuccessfullyAndReturnToken() {
        var request = new AuthRequest("user", "password123");
        var authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getName()).thenReturn("user");
        when(jwtUtil.generateToken("user")).thenReturn("token");

        AuthResponse response = authService.authenticate(request);

        assertNotNull(response);
        assertEquals("token", response.getToken());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtil, times(1)).generateToken("user");
    }

    @Test
    void shouldThrowBadCredentialsExceptionWhenAuthenticationFails() {
        var request = new AuthRequest("invalidUser", "password");
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Bad credentials"));

        var exception = assertThrows(BadCredentialsException.class, () -> authService.authenticate(request));

        assertEquals("Bad credentials", exception.getMessage());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verifyNoInteractions(jwtUtil);
    }
}