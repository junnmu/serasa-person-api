package com.serasa.personapi.infrastructure.exchange.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PersonResponse {
    private String name;
    private Integer age;
    private String phone;
    private String cep;
    private String state;
    private String city;
    private String neighborhood;
    private String street;
    private Integer score;
    private String scoreDescription;
}
