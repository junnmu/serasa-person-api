package com.serasa.personapi.domain.person.business;

import com.serasa.personapi.domain.person.Person;
import com.serasa.personapi.infrastructure.client.viacep.exchange.response.ViaCepResponse;
import com.serasa.personapi.infrastructure.exchange.request.PersonRequest;
import com.serasa.personapi.infrastructure.exchange.response.PersonResponse;
import com.serasa.personapi.infrastructure.repository.PersonRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;
    private final ViaCepService viaCepService;

    public PersonResponse create(PersonRequest request) {
        var address = viaCepService.getAddress(request.getCep());
        var person = buildPerson(request, address);

        return buildPersonResponse(personRepository.save(person));
    }

    private Person buildPerson(PersonRequest request, ViaCepResponse addressResponse) {
        return Person.builder()
            .name(request.getName())
            .age(request.getAge())
            .phone(request.getPhone())
            .cep(request.getCep())
            .state(addressResponse.getState())
            .city(addressResponse.getCity())
            .neighborhood(addressResponse.getNeighborhood())
            .street(addressResponse.getStreet())
            .score(request.getScore())
            .active(true)
            .build();
    }

    private PersonResponse buildPersonResponse(Person person) {
        return PersonResponse.builder()
            .name(person.getName())
            .age(person.getAge())
            .phone(person.getPhone())
            .cep(person.getCep())
            .state(person.getState())
            .city(person.getCity())
            .neighborhood(person.getNeighborhood())
            .street(person.getStreet())
            .score(person.getScore())
            .scoreDescription(getScoreDescription(person.getScore()))
            .build();
    }

    private String getScoreDescription(int score) {
        if (score <= 200) return "Insuficiente";
        if (score <= 500) return "Inaceitável";
        if (score <= 700) return "Aceitável";
        return "Recomendável";
    }
}
