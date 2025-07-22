package com.serasa.personapi.infrastructure.exchange.response;

import com.serasa.personapi.domain.person.Person;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
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

    public PersonResponse(Person person) {
        this.name = person.getName();
        this.age = person.getAge();
        this.cep = person.getCep();
        this.state = person.getState();
        this.city = person.getCity();
        this.neighborhood = person.getNeighborhood();
        this.street = person.getStreet();
        this.phone = person.getPhone();
        this.score = person.getScore();
        this.scoreDescription = person.getScoreDescription();
    }
}
