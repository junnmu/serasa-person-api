package com.serasa.personapi.domain.person.business;

import com.serasa.personapi.infrastructure.client.viacep.ViaCepClient;
import com.serasa.personapi.infrastructure.client.viacep.exchange.response.ViaCepResponse;
import com.serasa.personapi.infrastructure.exception.InvalidCepException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class ViaCepServiceTest {

    @Mock
    private ViaCepClient viaCepClient;

    @InjectMocks
    private ViaCepService viaCepService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnAddressWhenCepIsValid() {
        var cep = "12345678";
        var address = new ViaCepResponse();
        address.setState("SP");
        address.setCity("São Paulo");
        address.setNeighborhood("Centro");
        address.setStreet("Rua A");
        address.setError(false);

        when(viaCepClient.getAddressByCep(cep)).thenReturn(address);

        var result = viaCepService.getAddress(cep);

        assertThat(result).isEqualTo(address);

        verify(viaCepClient).getAddressByCep(cep);
        verifyNoMoreInteractions(viaCepClient);
    }

    @Test
    void shouldThrowExceptionWhenCepIsInvalid() {
        var cep = "87654321";
        var address = new ViaCepResponse();
        address.setError(true);

        when(viaCepClient.getAddressByCep(cep)).thenReturn(address);

        assertThrows(InvalidCepException.class, () -> viaCepService.getAddress(cep));

        verify(viaCepClient).getAddressByCep(cep);
        verifyNoMoreInteractions(viaCepClient);
    }
}