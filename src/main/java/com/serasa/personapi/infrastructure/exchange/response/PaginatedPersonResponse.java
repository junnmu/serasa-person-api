package com.serasa.personapi.infrastructure.exchange.response;

import com.serasa.personapi.domain.person.Person;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class PaginatedPersonResponse {
    private List<PersonResponse> persons;
    private PaginationResponse pagination;

    public PaginatedPersonResponse(List<Person> persons, Page<Person> page) {
        var paginationResponse = PaginationResponse.builder()
            .currentPage(page.getNumber())
            .itemsPerPage(page.getNumberOfElements())
            .numberOfPages(page.getTotalPages())
            .totalNumberOfItems(page.getTotalElements())
            .build();

        this.persons = persons.stream().map(PersonResponse::new).toList();
        this.pagination = paginationResponse;
    }
}
