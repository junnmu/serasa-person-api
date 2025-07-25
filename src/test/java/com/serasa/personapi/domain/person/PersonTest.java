package com.serasa.personapi.domain.person;

import com.serasa.personapi.infrastructure.client.viacep.exchange.response.ViaCepResponse;
import com.serasa.personapi.infrastructure.exchange.request.PersonUpdateRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonTest {

    @Test
    void shouldMergeWithNewValuesAndAddress() {
        var person = createPerson();

        var updateRequest = new PersonUpdateRequest();
        updateRequest.setName("New Name");
        updateRequest.setAge(40);
        updateRequest.setCep("98765432");
        updateRequest.setPhone("11912345678");
        updateRequest.setScore(600);

        var address = new ViaCepResponse();
        address.setState("RJ");
        address.setCity("Rio de Janeiro");
        address.setNeighborhood("Copacabana");
        address.setStreet("Rua Nova");

        var updated = person.merge(updateRequest, address);

        assertEquals("New Name", updated.getName());
        assertEquals(40, updated.getAge());
        assertEquals("98765432", updated.getCep());
        assertEquals("11912345678", updated.getPhone());
        assertEquals(600, updated.getScore());

        assertEquals("RJ", updated.getState());
        assertEquals("Rio de Janeiro", updated.getCity());
        assertEquals("Copacabana", updated.getNeighborhood());
        assertEquals("Rua Nova", updated.getStreet());
    }

    @Test
    void shouldMergeWithoutNewValuesAndAddress() {
        var person = createPerson();

        var updateRequest = new PersonUpdateRequest(); // all nulls

        var updated = person.merge(updateRequest, null);

        assertEquals("Original", updated.getName());
        assertEquals(25, updated.getAge());
        assertEquals("12345678", updated.getCep());
        assertEquals("11999999999", updated.getPhone());
        assertEquals(200, updated.getScore());

        assertEquals("SP", updated.getState());
        assertEquals("São Paulo", updated.getCity());
        assertEquals("Centro", updated.getNeighborhood());
        assertEquals("Rua A", updated.getStreet());
    }

    @Test
    void shouldReturnScoreDescription() {
        var person = new Person();
        person.setScore(100);

        var description = person.getScoreDescription();

        assertEquals("Insuficiente", description);
    }

    @Test
    void shouldReturnUnknownScoreDescriptionForInvalidScore() {
        var person = new Person();
        person.setScore(-1);

        var description = person.getScoreDescription();

        assertEquals("Unknown", description);
    }

    private Person createPerson() {
        var person = new Person();
        person.setName("Original");
        person.setAge(25);
        person.setCep("12345678");
        person.setPhone("11999999999");
        person.setScore(200);
        person.setState("SP");
        person.setCity("São Paulo");
        person.setNeighborhood("Centro");
        person.setStreet("Rua A");
        return person;
    }
}
