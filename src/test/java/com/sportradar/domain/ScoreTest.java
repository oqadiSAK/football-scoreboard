package com.sportradar.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Score")
class ScoreTest {

    @Nested
    @DisplayName("creation")
    class Creation {
        @Test
        @DisplayName("succeeds with valid scores")
        void shouldCreateWithValidScores() {
            // When
            var score = new Score(2, 3);

            // Then
            assertEquals(2, score.getHomeScore());
            assertEquals(3, score.getAwayScore());
            assertEquals(5, score.getTotal());
        }

        @ParameterizedTest(name = "fails with negative scores: home={0}, away={1}")
        @CsvSource({
                "-1, 0",
                "0, -1",
                "-2, -2"
        })
        @DisplayName("fails with negative scores")
        void shouldThrowExceptionForNegativeScores(int homeScore, int awayScore) {
            // When/Then
            var exception = assertThrows(IllegalArgumentException.class,
                    () -> new Score(homeScore, awayScore));
            assertTrue(exception.getMessage().contains("cannot be negative"));
        }
    }

    @Test
    @DisplayName("provides initial zero score")
    void initialScoreShouldBeZeroZero() {
        // When
        var score = Score.initial();

        // Then
        assertEquals(0, score.getHomeScore());
        assertEquals(0, score.getAwayScore());
        assertEquals(0, score.getTotal());
    }

    @Nested
    @DisplayName("equality")
    class Equality {
        @Test
        @DisplayName("considers identical scores equal")
        void shouldConsiderIdenticalScoresEqual() {
            // Given
            var score1 = new Score(1, 1);
            var score2 = new Score(1, 1);

            // Then
            assertEquals(score1, score2);
            assertEquals(score1.hashCode(), score2.hashCode());
        }

        @Test
        @DisplayName("considers different scores not equal")
        void shouldConsiderDifferentScoresNotEqual() {
            // Given
            var score1 = new Score(1, 1);
            var score2 = new Score(1, 2);

            // Then
            assertNotEquals(score1, score2);
        }
    }

    @Test
    @DisplayName("formats scores correctly in toString()")
    void shouldFormatToStringCorrectly() {
        // Given
        var score = new Score(2, 3);

        // Then
        assertEquals("2 - 3", score.toString());
    }
}