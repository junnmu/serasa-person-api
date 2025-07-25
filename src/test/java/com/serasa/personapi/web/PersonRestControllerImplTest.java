package com.serasa.personapi.web;

import com.serasa.personapi.domain.person.business.PersonService;
import com.serasa.personapi.infrastructure.exchange.request.PersonRequest;
import com.serasa.personapi.infrastructure.exchange.request.PersonUpdateRequest;
import com.serasa.personapi.infrastructure.exchange.request.params.PersonSearchParams;
import com.serasa.personapi.infrastructure.exchange.response.PaginatedPersonResponse;
import com.serasa.personapi.infrastructure.exchange.response.PaginationResponse;
import com.serasa.personapi.infrastructure.exchange.response.PersonResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PersonRestControllerImplTest {

    private PersonService personService;
    private PersonRestControllerImpl controller;

    @BeforeEach
    void setUp() {
        personService = mock(PersonService.class);
        controller = new PersonRestControllerImpl(personService);
    }

    @Test
    void shouldCreatePerson() {
        var request = new PersonRequest("John", 30, "11999998888", "12345678", 500);
        var expectedResponse = new PersonResponse(
            "John", 30, "11999998888", "12345678", "SP",
            "São Paulo", "Centro", "Rua A", 1000, "Recomendável"
        );

        when(personService.create(request)).thenReturn(expectedResponse);

        var response = controller.create(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(expectedResponse);
        verify(personService).create(request);
    }

    @Test
    void shouldSearchPersonsWithParams() {
        var searchParams = new PersonSearchParams("Ana", 25, "99999999", 0, 5);
        var personResponse = new PersonResponse(
            "Ana", 25, "11999997777", "99999999", "SP",
            "Campinas", "Centro", "Rua B", 1000, "Recomendável"
        );

        var pagination = PaginationResponse.builder()
            .currentPage(0)
            .itemsPerPage(1)
            .numberOfPages(1)
            .totalNumberOfItems(1L)
            .build();

        var expectedResponse = new PaginatedPersonResponse(List.of(personResponse), pagination);

        when(personService.search(searchParams)).thenReturn(expectedResponse);

        var response = controller.search("Ana", 25, "99999999", 0, 5);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        var actual = response.getBody();

        assertEquals(expectedResponse.getPagination().getCurrentPage(), actual.getPagination().getCurrentPage());
        assertEquals(expectedResponse.getPagination().getItemsPerPage(), actual.getPagination().getItemsPerPage());
        assertEquals(expectedResponse.getPagination().getNumberOfPages(), actual.getPagination().getNumberOfPages());
        assertEquals(expectedResponse.getPagination().getTotalNumberOfItems(), actual.getPagination().getTotalNumberOfItems());

        assertEquals(1, actual.getPersons().size());
        var actualPerson = actual.getPersons().get(0);
        assertEquals("Ana", actualPerson.getName());
        assertEquals(25, actualPerson.getAge());
        assertEquals("11999997777", actualPerson.getPhone());
        assertEquals("99999999", actualPerson.getCep());
        assertEquals("SP", actualPerson.getState());
        assertEquals("Campinas", actualPerson.getCity());
        assertEquals("Centro", actualPerson.getNeighborhood());
        assertEquals("Rua B", actualPerson.getStreet());
        assertEquals(1000, actualPerson.getScore());
        assertEquals("Recomendável", actualPerson.getScoreDescription());

        verify(personService).search(searchParams);
    }

    @Test
    void shouldUpdatePersonById() {
        var updateRequest = new PersonUpdateRequest();
        updateRequest.setName("New Name");
        updateRequest.setAge(35);
        updateRequest.setPhone("11988887777");
        updateRequest.setCep("87654321");
        updateRequest.setScore(700);

        var expectedResponse = new PersonResponse(
            "New Name", 35, "11988887777", "87654321", "RJ",
            "Rio de Janeiro", "Zona Sul", "Rua C", 1000, "Recomendável"
        );

        when(personService.update(1L, updateRequest)).thenReturn(expectedResponse);

        var response = controller.update(1L, updateRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedResponse);
        verify(personService).update(1L, updateRequest);
    }

    @Test
    void shouldDeletePersonById() {
        doNothing().when(personService).delete(1L);

        var response = controller.delete(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(personService).delete(1L);
    }
}