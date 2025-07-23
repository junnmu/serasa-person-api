package com.serasa.personapi.domain.person.business;

import com.serasa.personapi.infrastructure.client.viacep.ViaCepClient;
import com.serasa.personapi.infrastructure.client.viacep.exchange.response.ViaCepResponse;
import com.serasa.personapi.infrastructure.exception.InvalidCepException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class ViaCepService {

    private ViaCepClient viaCepClient;

    public ViaCepResponse getAddress(String cep) {
        log.info("class=ViaCepService, method=getAddress, info=Searching address by CEP");

        var response = viaCepClient.getAddressByCep(cep);

        if (Boolean.TRUE.equals(response.getError())) {
            log.info("class=ViaCepService, method=getAddress, info=Invalid CEP, cep={}", cep);
            throw new InvalidCepException("Invalid CEP: " + cep);
        }
        return response;
    }
}
