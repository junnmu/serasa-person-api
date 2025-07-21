package com.serasa.personapi.domain.person.business;

import com.serasa.personapi.infrastructure.client.viacep.ViaCepClient;
import com.serasa.personapi.infrastructure.client.viacep.exchange.response.ViaCepResponse;
import com.serasa.personapi.infrastructure.exception.InvalidCepException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ViaCepService {

    private ViaCepClient viaCepClient;

    public ViaCepResponse getAddress(String cep) {
        var response = viaCepClient.getAddressByCep(cep);

        if (Boolean.TRUE.equals(response.getError())) {
            throw new InvalidCepException("Invalid CEP: " + cep);
        }
        return response;
    }
}
