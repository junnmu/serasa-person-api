package com.serasa.personapi.domain.person.business;

import com.serasa.personapi.domain.person.Person;
import com.serasa.personapi.infrastructure.client.viacep.exchange.response.ViaCepResponse;
import com.serasa.personapi.infrastructure.exception.PersonNotFoundException;
import com.serasa.personapi.infrastructure.exchange.request.PersonRequest;
import com.serasa.personapi.infrastructure.exchange.request.PersonUpdateRequest;
import com.serasa.personapi.infrastructure.exchange.request.params.PersonSearchParams;
import com.serasa.personapi.infrastructure.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private ViaCepService viaCepService;

    @InjectMocks
    private PersonService personService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreatePersonSuccessfully() {
        var request = new PersonRequest("John", 30, "11999999999", "12345678", 500);
        var address = TestHelper.buildAddress();
        var person = new Person(request, address);

        when(viaCepService.getAddress(request.getCep())).thenReturn(address);
        when(personRepository.save(any(Person.class))).thenReturn(person);

        var response = personService.create(request);

        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("John");

        verify(viaCepService, times(1)).getAddress(request.getCep());
        verify(personRepository, times(1)).save(any(Person.class));
    }

    @Test
    void shouldSearchPeopleSuccessfully() {
        var searchParams = new PersonSearchParams(null, null, null, 0, 10);

        var person = new Person(null, "Maria", null, null, null, null, null, null, null, 1000, true);
        var page = new PageImpl<>(Collections.singletonList(person));

        when(personRepository.findAll(any(Example.class), any(PageRequest.class))).thenReturn(page);

        var result = personService.search(searchParams);

        assertThat(result.getPersons()).hasSize(1);
        assertThat(result.getPersons().get(0).getName()).isEqualTo("Maria");

        verify(personRepository, times(1)).findAll(any(Example.class), any(PageRequest.class));
    }

    @Test
    void shouldUpdatePersonWithNewAddress() {
        var request = new PersonUpdateRequest();
        request.setCep("98765432");

        var existing = mock(Person.class);
        var address = TestHelper.buildAddress();
        var updated = mock(Person.class);

        when(personRepository.findByIdAndActiveTrue(1L)).thenReturn(Optional.of(existing));
        when(viaCepService.getAddress("98765432")).thenReturn(address);
        when(existing.merge(request, address)).thenReturn(updated);
        when(personRepository.save(updated)).thenReturn(updated);

        var response = personService.update(1L, request);

        assertThat(response).isNotNull();

        verify(viaCepService, times(1)).getAddress(request.getCep());
        verify(personRepository, times(1)).save(updated);
    }

    @Test
    void shouldUpdatePersonWithoutNewAddress() {
        var request = new PersonUpdateRequest();
        request.setCep(null);

        var existing = mock(Person.class);
        var updated = mock(Person.class);

        when(personRepository.findByIdAndActiveTrue(1L)).thenReturn(Optional.of(existing));
        when(existing.merge(request, null)).thenReturn(updated);
        when(personRepository.save(updated)).thenReturn(updated);

        var response = personService.update(1L, request);

        assertThat(response).isNotNull();

        verify(personRepository, times(1)).findByIdAndActiveTrue(1L);
        verify(personRepository, times(1)).save(updated);
    }

    @Test
    void shouldThrowPersonNotFoundExceptionWhenPersonToUpdateNotFound() {
        var request = new PersonUpdateRequest();

        when(personRepository.findByIdAndActiveTrue(1L)).thenReturn(Optional.empty());

        assertThrows(PersonNotFoundException.class, () -> personService.update(1L, request));

        verify(personRepository, times(1)).findByIdAndActiveTrue(1L);
        verifyNoMoreInteractions(personRepository);
    }

    @Test
    void shouldDeletePersonSuccessfully() {
        var person = mock(Person.class);

        when(personRepository.findByIdAndActiveTrue(1L)).thenReturn(Optional.of(person));

        personService.delete(1L);

        verify(person).setActive(false);
        verify(personRepository, times(1)).findByIdAndActiveTrue(1L);
        verify(personRepository, times(1)).save(person);
    }

    @Test
    void shouldThrowPersonNotFoundExceptionWhenPersonToDeleteNotFound() {
        when(personRepository.findByIdAndActiveTrue(1L)).thenReturn(Optional.empty());

        assertThrows(PersonNotFoundException.class, () -> personService.delete(1L));

        verify(personRepository, times(1)).findByIdAndActiveTrue(1L);
        verifyNoMoreInteractions(personRepository);
    }

    private static class TestHelper {
        static ViaCepResponse buildAddress() {
            var address = new ViaCepResponse();
            address.setState("SP");
            address.setCity("São Paulo");
            address.setNeighborhood("Centro");
            address.setStreet("Rua A");
            return address;
        }
    }
}
