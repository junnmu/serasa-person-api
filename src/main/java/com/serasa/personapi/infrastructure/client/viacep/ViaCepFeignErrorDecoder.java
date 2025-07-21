package com.serasa.personapi.infrastructure.client.viacep;

import com.serasa.personapi.infrastructure.exception.ViaCepBadRequestException;
import com.serasa.personapi.infrastructure.exception.ViaCepIntegrationException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

public class ViaCepFeignErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder errorDecoder = new ErrorDecoder.Default();

    @Override
    public Exception decode(String s, Response response) {
        if (HttpStatus.valueOf(response.status()) == HttpStatus.BAD_REQUEST) {
            throw new ViaCepBadRequestException("ViaCep returning a BAD REQUEST");
        } else {
            throw new ViaCepIntegrationException(errorDecoder.decode(s, response));
        }
    }
}
