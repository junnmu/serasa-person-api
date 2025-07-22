package com.serasa.personapi.infrastructure.exchange.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PaginatedPersonResponse {
    private List<PersonResponse> persons;
    private PaginationResponse pagination;
}
