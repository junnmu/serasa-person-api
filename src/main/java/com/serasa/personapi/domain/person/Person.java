package com.serasa.personapi.domain.person;

import com.serasa.personapi.infrastructure.client.viacep.exchange.response.ViaCepResponse;
import com.serasa.personapi.infrastructure.exchange.request.PersonRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "persons")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer age;
    private String cep;
    private String state;
    private String city;
    private String neighborhood;
    private String street;
    private String phone;
    private Integer score;
    private Boolean active;

    public Person(PersonRequest request, ViaCepResponse address) {
        this.name = request.getName();
        this.age = request.getAge();
        this.cep = request.getCep();
        this.state = address.getState();
        this.city = address.getCity();
        this.neighborhood = address.getNeighborhood();
        this.street = address.getStreet();
        this.phone = request.getPhone();
        this.score = request.getScore();
        this.active = true;
    }

    public String getScoreDescription() {
        if (score <= 200) return ScoreDescription.INSUFFICIENT.getValue();
        if (score <= 500) return ScoreDescription.UNACCEPTABLE.getValue();
        if (score <= 700) return ScoreDescription.ACCEPTABLE.getValue();
        return ScoreDescription.RECOMMENDED.getValue();
    }
}
