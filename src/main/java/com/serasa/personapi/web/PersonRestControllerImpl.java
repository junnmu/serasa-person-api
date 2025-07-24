package com.serasa.personapi.web;

import com.serasa.personapi.domain.person.business.PersonService;
import com.serasa.personapi.infrastructure.exchange.request.PersonRequest;
import com.serasa.personapi.infrastructure.exchange.request.PersonUpdateRequest;
import com.serasa.personapi.infrastructure.exchange.request.params.PersonSearchParams;
import com.serasa.personapi.infrastructure.exchange.response.PaginatedPersonResponse;
import com.serasa.personapi.infrastructure.exchange.response.PersonResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/persons")
@AllArgsConstructor
public class PersonRestControllerImpl implements PersonRestController {

    private final PersonService personService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PersonResponse> create(@RequestBody @Valid PersonRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(personService.create(request));
    }

    @GetMapping
    public ResponseEntity<PaginatedPersonResponse> search(
        @RequestParam(value = "name", required = false) String name,
        @RequestParam(value = "age", required = false) Integer age,
        @RequestParam(value = "cep", required = false) String cep,
        @RequestParam(value = "page", defaultValue = "0") Integer page,
        @RequestParam(value = "size", defaultValue = "5") Integer size
    ) {
        var searchParams = new PersonSearchParams(name, age, cep, page, size);
        return ResponseEntity.ok(personService.search(searchParams));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PersonResponse> update(
        @PathVariable("id") Long id,
        @RequestBody @Valid PersonUpdateRequest request
    ) {
        return ResponseEntity.ok().body(personService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        personService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
