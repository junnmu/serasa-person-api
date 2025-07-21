package com.serasa.personapi.web;

import com.serasa.personapi.domain.person.business.PersonService;
import com.serasa.personapi.infrastructure.exchange.request.PersonRequest;
import com.serasa.personapi.infrastructure.exchange.response.PersonResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
