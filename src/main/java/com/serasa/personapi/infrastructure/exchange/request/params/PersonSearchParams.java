package com.serasa.personapi.infrastructure.exchange.request.params;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PersonSearchParams {
    private String name;
    private Integer age;
    private String cep;
    private Integer currentPage;
    private Integer itemsPerPage;
}
