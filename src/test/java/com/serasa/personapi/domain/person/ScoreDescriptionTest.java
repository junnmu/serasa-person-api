package com.serasa.personapi.domain.person;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ScoreDescriptionTest {

    @Test
    void shouldReturnTrueWhenScoreIsWithinRange() {
        assertTrue(ScoreDescription.INSUFFICIENT.inRange(0));
        assertTrue(ScoreDescription.INSUFFICIENT.inRange(200));

        assertTrue(ScoreDescription.UNACCEPTABLE.inRange(201));
        assertTrue(ScoreDescription.UNACCEPTABLE.inRange(500));

        assertTrue(ScoreDescription.ACCEPTABLE.inRange(501));
        assertTrue(ScoreDescription.ACCEPTABLE.inRange(700));

        assertTrue(ScoreDescription.RECOMMENDED.inRange(701));
        assertTrue(ScoreDescription.RECOMMENDED.inRange(1000));
    }

    @Test
    void shouldReturnFalseWhenScoreIsOutsideRange() {
        assertFalse(ScoreDescription.INSUFFICIENT.inRange(-1));
        assertFalse(ScoreDescription.INSUFFICIENT.inRange(201));

        assertFalse(ScoreDescription.UNACCEPTABLE.inRange(500 + 1));
        assertFalse(ScoreDescription.UNACCEPTABLE.inRange(200));

        assertFalse(ScoreDescription.ACCEPTABLE.inRange(700 + 1));
        assertFalse(ScoreDescription.ACCEPTABLE.inRange(500));

        assertFalse(ScoreDescription.RECOMMENDED.inRange(1000 + 1));
        assertFalse(ScoreDescription.RECOMMENDED.inRange(700));
    }
}