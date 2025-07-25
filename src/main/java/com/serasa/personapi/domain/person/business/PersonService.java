package com.serasa.personapi.domain.person.business;

import com.serasa.personapi.domain.person.Person;
import com.serasa.personapi.infrastructure.exception.PersonNotFoundException;
import com.serasa.personapi.infrastructure.exchange.request.PersonRequest;
import com.serasa.personapi.infrastructure.exchange.request.PersonUpdateRequest;
import com.serasa.personapi.infrastructure.exchange.request.params.PersonSearchParams;
import com.serasa.personapi.infrastructure.exchange.response.PaginatedPersonResponse;
import com.serasa.personapi.infrastructure.exchange.response.PersonResponse;
import com.serasa.personapi.infrastructure.repository.PersonRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;
    private final ViaCepService viaCepService;

    public PersonResponse create(PersonRequest request) {
        log.info("class=PersonService, method=create, info=Starting creation flow");

        var address = viaCepService.getAddress(request.getCep());
        var person = new Person(request, address);

        return new PersonResponse(personRepository.save(person));
    }

    public PaginatedPersonResponse search(PersonSearchParams searchParams) {
        log.info("class=PersonService, method=search, info=Starting search flow");

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
        return new PaginatedPersonResponse(page.getContent(), page);
    }

    public PersonResponse update(Long id, PersonUpdateRequest request) {
        log.info("class=PersonService, method=update, info=Starting update flow");

        var person = getPerson(id);
        var address = request.getCep() != null ? viaCepService.getAddress(request.getCep()) : null;

        return new PersonResponse(personRepository.save(person.merge(request, address)));
    }

    public void delete(Long id) {
        log.info("class=PersonService, method=delete, info=Starting delete flow");

        var person = getPerson(id);

        person.setActive(false);
        personRepository.save(person);
    }

    private Person getPerson(Long id) {
        return personRepository.findByIdAndActiveTrue(id)
            .orElseThrow(() -> new PersonNotFoundException("Person not found"));
    }
}
