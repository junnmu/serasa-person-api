package com.serasa.personapi.web;

import com.serasa.personapi.domain.auth.business.AuthService;
import com.serasa.personapi.infrastructure.exchange.request.AuthRequest;
import com.serasa.personapi.infrastructure.exchange.response.AuthResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthRestControllerImplTest {

    private AuthService authService;
    private AuthRestControllerImpl controller;

    @BeforeEach
    void setUp() {
        authService = mock(AuthService.class);
        controller = new AuthRestControllerImpl(authService);
    }

    @Test
    void shouldReturnAuthResponseWhenLoginIsCalled() {
        var request = new AuthRequest("user", "pass");
        var expectedResponse = new AuthResponse("token");

        when(authService.authenticate(request)).thenReturn(expectedResponse);

        var response = controller.login(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedResponse, response.getBody());
        verify(authService).authenticate(request);
    }
}