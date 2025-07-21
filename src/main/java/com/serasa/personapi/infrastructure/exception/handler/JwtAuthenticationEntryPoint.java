package com.serasa.personapi.infrastructure.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.serasa.personapi.infrastructure.exchange.error.ErrorMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.OutputStream;

@Component
@AllArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void commence(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException authException
    ) {
        try {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            ErrorMessage error = new ErrorMessage("SER-10006", "Invalid or expired token");

            OutputStream out = response.getOutputStream();
            mapper.writeValue(out, error);
            out.flush();
        } catch (Exception e) {
            throw new RuntimeException("Error writing 401 response", e);
        }
    }
}
