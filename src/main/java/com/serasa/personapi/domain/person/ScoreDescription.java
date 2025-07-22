package com.serasa.personapi.domain.person;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ScoreDescription {
    INSUFFICIENT("Insuficiente"),
    UNACCEPTABLE("Inaceitável"),
    ACCEPTABLE("Aceitável"),
    RECOMMENDED("Recomendável");

    private final String value;
}
