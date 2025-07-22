package com.serasa.personapi.web;

import com.serasa.personapi.domain.person.business.PersonService;
import com.serasa.personapi.infrastructure.exchange.request.PersonRequest;
import com.serasa.personapi.infrastructure.exchange.request.params.PersonSearchParams;
import com.serasa.personapi.infrastructure.exchange.response.PaginatedPersonResponse;
import com.serasa.personapi.infrastructure.exchange.response.PersonResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/persons")
@AllArgsConstructor
public class PersonRestController {

    private final PersonService personService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PersonResponse> create(@RequestBody @Valid PersonRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(personService.create(request));
    }

    @GetMapping
    public ResponseEntity<PaginatedPersonResponse> searchPersons(
        @RequestParam(value = "name", required = false) String name,
        @RequestParam(value = "age", required = false) Integer age,
        @RequestParam(value = "cep", required = false) String cep,
        @RequestParam(value = "page", defaultValue = "0") Integer page,
        @RequestParam(value = "size", defaultValue = "5") Integer size
    ) {
        var searchParams = new PersonSearchParams(name, age, cep, page, size);

        return ResponseEntity.ok(personService.searchPersons(searchParams));
    }
}
