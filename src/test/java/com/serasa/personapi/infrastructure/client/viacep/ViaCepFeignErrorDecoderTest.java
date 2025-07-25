package com.serasa.personapi.infrastructure.client.viacep;

import com.serasa.personapi.infrastructure.exception.ViaCepBadRequestException;
import com.serasa.personapi.infrastructure.exception.ViaCepIntegrationException;
import feign.Request;
import feign.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ViaCepFeignErrorDecoderTest {

    private final ViaCepFeignErrorDecoder decoder = new ViaCepFeignErrorDecoder();

    @Test
    void shouldThrowBadRequestExceptionWhenStatusIs400() {
        var response = Response.builder()
            .status(HttpStatus.BAD_REQUEST.value())
            .reason("Bad Request")
            .request(Request.create(Request.HttpMethod.GET, "/some-url", Collections.emptyMap(), null, StandardCharsets.UTF_8))
            .build();

        var exception = assertThrows(ViaCepBadRequestException.class, () -> decoder.decode("methodKey", response));
        assertEquals("ViaCep returning a BAD REQUEST", exception.getMessage());
    }

    @Test
    void shouldThrowIntegrationExceptionWhenStatusIsNot400() {
        var response = Response.builder()
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .reason("Internal Server Error")
            .request(Request.create(Request.HttpMethod.GET, "/some-url", Collections.emptyMap(), null, StandardCharsets.UTF_8))
            .build();

        var exception = assertThrows(ViaCepIntegrationException.class, () -> decoder.decode("methodKey", response));
        assertNotNull(exception.getMessage());
        assertNotNull(exception.getCause());
    }
}