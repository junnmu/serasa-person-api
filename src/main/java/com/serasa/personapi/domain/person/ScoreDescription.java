package com.serasa.personapi.domain.person;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ScoreDescription {
    INSUFFICIENT(0, 200, "Insuficiente"),
    UNACCEPTABLE(201, 500, "Inaceitável"),
    ACCEPTABLE(501, 700, "Aceitável"),
    RECOMMENDED(701, 1000, "Recomendável");

    private final int min;
    private final int max;
    private final String value;

    public boolean inRange(int score) {
        return score >= min && score <= max;
    }
}
