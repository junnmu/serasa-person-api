package com.serasa.personapi.infrastructure.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.serasa.personapi.infrastructure.exchange.error.ErrorMessage;
import com.serasa.personapi.infrastructure.exchange.error.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.util.List;

@Component
@AllArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper mapper = new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

    @Override
    public void handle(
        HttpServletRequest request,
        HttpServletResponse response,
        AccessDeniedException accessDeniedException
    ) {
        try {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            var errorResponse = new ErrorResponse(
                List.of(new ErrorMessage("SER-10005", "You do not have permission to access this resource"))
            );

            OutputStream out = response.getOutputStream();
            mapper.writeValue(out, errorResponse);
            out.flush();
        } catch (Exception e) {
            throw new RuntimeException("Error writing 403 response", e);
        }
    }
}
