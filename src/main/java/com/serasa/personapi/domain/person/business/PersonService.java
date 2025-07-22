package com.serasa.personapi.domain.person.business;

import com.serasa.personapi.domain.person.Person;
import com.serasa.personapi.infrastructure.client.viacep.exchange.response.ViaCepResponse;
import com.serasa.personapi.infrastructure.exchange.request.PersonRequest;
import com.serasa.personapi.infrastructure.exchange.request.params.PersonSearchParams;
import com.serasa.personapi.infrastructure.exchange.response.PaginatedPersonResponse;
import com.serasa.personapi.infrastructure.exchange.response.PaginationResponse;
import com.serasa.personapi.infrastructure.exchange.response.PersonResponse;
import com.serasa.personapi.infrastructure.repository.PersonRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public PaginatedPersonResponse searchPersons(PersonSearchParams searchParams) {
        var pageable = PageRequest.of(searchParams.getCurrentPage(), searchParams.getItemsPerPage());
        var filter = new Person();

        filter.setActive(true);
        if (searchParams.getName() != null) filter.setName(searchParams.getName());
        if (searchParams.getAge() != null) filter.setAge(searchParams.getAge());
        if (searchParams.getCep() != null) filter.setCep(searchParams.getCep());

        ExampleMatcher matcher = ExampleMatcher.matchingAll()
            .withIgnoreCase()
            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        var page = personRepository.findAll(Example.of(filter, matcher), pageable);
        return buildPaginatedPersonResponse(page.getContent(), page);
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

    private PaginatedPersonResponse buildPaginatedPersonResponse(List<Person> persons, Page<Person> page) {
        var paginationResponse = PaginationResponse.builder()
            .currentPage(page.getNumber())
            .itemsPerPage(page.getNumberOfElements())
            .numberOfPages(page.getTotalPages())
            .totalNumberOfItems(page.getTotalElements())
            .build();

        return PaginatedPersonResponse.builder()
            .persons(persons.stream().map(this::buildPersonResponse).toList())
            .pagination(paginationResponse)
            .build();
    }

    private String getScoreDescription(int score) {
        if (score <= 200) return "Insuficiente";
        if (score <= 500) return "Inaceitável";
        if (score <= 700) return "Aceitável";
        return "Recomendável";
    }
}
