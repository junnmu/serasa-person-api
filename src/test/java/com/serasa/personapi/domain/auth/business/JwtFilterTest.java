package com.serasa.personapi.domain.auth.business;

import com.serasa.personapi.infrastructure.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class JwtFilterTest {

    private JwtUtil jwtUtil;
    private CustomUserDetailsService userDetailsService;
    private JwtFilter jwtFilter;

    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        jwtUtil = mock(JwtUtil.class);
        userDetailsService = mock(CustomUserDetailsService.class);
        jwtFilter = new JwtFilter(jwtUtil, userDetailsService);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);

        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldNotAuthenticateWhenAuthorizationHeaderIsMissing() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtFilter.doFilterInternal(request, response, filterChain);

        var authentication = SecurityContextHolder.getContext().getAuthentication();

        assertNull(authentication);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtUtil, userDetailsService);
    }

    @Test
    void shouldNotAuthenticateWhenTokenIsInvalidFormat() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Token wrong");

        jwtFilter.doFilterInternal(request, response, filterChain);

        var authentication = SecurityContextHolder.getContext().getAuthentication();

        assertNull(authentication);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtUtil, userDetailsService);
    }

    @Test
    void shouldNotAuthenticateWhenUsernameIsNull() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer token");
        when(jwtUtil.getUsernameFromToken("token")).thenReturn(null);

        jwtFilter.doFilterInternal(request, response, filterChain);

        var authentication = SecurityContextHolder.getContext().getAuthentication();

        assertNull(authentication);

        verify(jwtUtil).getUsernameFromToken("token");
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldNotAuthenticateWhenContextAlreadyHasAuthentication() throws ServletException, IOException {
        var auth = mock(UsernamePasswordAuthenticationToken.class);
        SecurityContextHolder.getContext().setAuthentication(auth);

        when(request.getHeader("Authorization")).thenReturn("Bearer token");
        when(jwtUtil.getUsernameFromToken("token")).thenReturn("marcelo");

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(jwtUtil).getUsernameFromToken("token");
        verifyNoMoreInteractions(jwtUtil, userDetailsService);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldNotAuthenticateWhenTokenIsInvalid() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer token");
        when(jwtUtil.getUsernameFromToken("token")).thenReturn("marcelo");

        var userDetails = new User("marcelo", "pass", Collections.emptyList());
        when(userDetailsService.loadUserByUsername("marcelo")).thenReturn(userDetails);
        when(jwtUtil.isValidToken("token", userDetails)).thenReturn(false);

        jwtFilter.doFilterInternal(request, response, filterChain);

        var authentication = SecurityContextHolder.getContext().getAuthentication();

        assertNull(authentication);

        verify(jwtUtil).getUsernameFromToken("token");
        verify(userDetailsService).loadUserByUsername("marcelo");
        verify(jwtUtil).isValidToken("token", userDetails);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldAuthenticateWhenTokenIsValid() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer token");
        when(jwtUtil.getUsernameFromToken("token")).thenReturn("marcelo");

        var userDetails = new User("marcelo", "pass", Collections.emptyList());
        when(userDetailsService.loadUserByUsername("marcelo")).thenReturn(userDetails);
        when(jwtUtil.isValidToken("token", userDetails)).thenReturn(true);

        jwtFilter.doFilterInternal(request, response, filterChain);

        var authentication = SecurityContextHolder.getContext().getAuthentication();

        assertNotNull(authentication);
        assertEquals("marcelo", authentication.getName());

        verify(jwtUtil).getUsernameFromToken("token");
        verify(userDetailsService).loadUserByUsername("marcelo");
        verify(jwtUtil).isValidToken("token", userDetails);
        verify(filterChain).doFilter(request, response);
    }
}