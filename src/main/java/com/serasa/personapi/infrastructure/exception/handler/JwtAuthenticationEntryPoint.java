package com.serasa.personapi.infrastructure.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.serasa.personapi.infrastructure.exchange.error.ErrorMessage;
import com.serasa.personapi.infrastructure.exchange.error.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.util.List;

@Component
@AllArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper mapper = new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

    @Override
    public void commence(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException authException
    ) {
        try {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            var errorResponse = new ErrorResponse(
                List.of(new ErrorMessage("SER-10006", "Invalid or expired token"))
            );

            OutputStream out = response.getOutputStream();
            mapper.writeValue(out, errorResponse);
            out.flush();
        } catch (Exception e) {
            throw new RuntimeException("Error writing 401 response", e);
        }
    }
}
