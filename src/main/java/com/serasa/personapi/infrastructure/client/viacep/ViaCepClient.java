package com.serasa.personapi.infrastructure.client.viacep;

import com.serasa.personapi.infrastructure.client.viacep.exchange.response.ViaCepResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
    name = "viaCepClient",
    url = "https://viacep.com.br/ws",
    configuration = ViaCepClient.ViaCepFeignConfiguration.class
)
public interface ViaCepClient {

    @GetMapping("/{cep}/json")
    ViaCepResponse getAddressByCep(@PathVariable("cep") String cep);

    class ViaCepFeignConfiguration {

        @Bean(value = "ViaCepErrorDecoder")
        public ViaCepFeignErrorDecoder viaCepErrorDecoder() {
            return new ViaCepFeignErrorDecoder();
        }
    }
}
