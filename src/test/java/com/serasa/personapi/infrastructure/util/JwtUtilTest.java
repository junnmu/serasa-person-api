package com.serasa.personapi.infrastructure.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
    }

    @Test
    void shouldGenerateTokenWithCorrectSubject() {
        var token = jwtUtil.generateToken("testuser");
        var subject = jwtUtil.getUsernameFromToken(token);

        assertEquals("testuser", subject);
    }

    @Test
    void shouldReturnTrueForValidTokenAndMatchingUsername() {
        var userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testuser");

        var token = jwtUtil.generateToken("testuser");

        assertTrue(jwtUtil.isValidToken(token, userDetails));
    }

    @Test
    void shouldReturnFalseForInvalidToken() {
        var userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("someone");

        var result = jwtUtil.isValidToken("invalid.token.structure", userDetails);

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenUsernameDoesNotMatch() {
        var token = jwtUtil.generateToken("testuser");

        var userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("wronguser");

        assertFalse(jwtUtil.isValidToken(token, userDetails));
    }
}