package com.sportradar.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Match")
class MatchTest {

    @Nested
    @DisplayName("creation")
    class Creation {
        @Test
        @DisplayName("succeeds with valid teams")
        void shouldCreateWithValidTeams() {
            // Given
            var homeTeam = new Team("Brazil");
            var awayTeam = new Team("Germany");

            // When
            var match = new Match(homeTeam, awayTeam);

            // Then
            assertAll(
                    () -> assertEquals(homeTeam, match.getHomeTeam()),
                    () -> assertEquals(awayTeam, match.getAwayTeam()),
                    () -> assertEquals(0, match.getHomeScore()),
                    () -> assertEquals(0, match.getAwayScore()),
                    () -> assertEquals(0, match.getTotalScore()),
                    () -> assertNotNull(match.getStartTime()));
        }

        @Test
        @DisplayName("fails with null home team")
        void shouldThrowExceptionForNullHomeTeam() {
            // Given
            var awayTeam = new Team("Germany");

            // When/Then
            var exception = assertThrows(IllegalArgumentException.class,
                    () -> new Match(null, awayTeam));
            assertTrue(exception.getMessage().contains("Home team cannot be null"));
        }

        @Test
        @DisplayName("fails with null away team")
        void shouldThrowExceptionForNullAwayTeam() {
            // Given
            var homeTeam = new Team("Brazil");

            // When/Then
            var exception = assertThrows(IllegalArgumentException.class,
                    () -> new Match(homeTeam, null));
            assertTrue(exception.getMessage().contains("Away team cannot be null"));
        }

        @Test
        @DisplayName("fails when teams are the same")
        void shouldThrowExceptionForSameTeams() {
            // Given
            var team = new Team("Brazil");

            // When/Then
            var exception = assertThrows(IllegalArgumentException.class,
                    () -> new Match(team, team));
            assertTrue(exception.getMessage().contains("cannot be the same"));
        }
    }

    @Nested
    @DisplayName("score management")
    class ScoreManagement {
        @Test
        @DisplayName("updates score correctly")
        void shouldUpdateScore() {
            // Given
            var match = new Match(new Team("Brazil"), new Team("Germany"));
            var newScore = new Score(3, 2);

            // When
            match.updateScore(newScore);

            // Then
            assertEquals(3, match.getHomeScore());
            assertEquals(2, match.getAwayScore());
            assertEquals(5, match.getTotalScore());
        }

        @Test
        @DisplayName("fails when updating with null score")
        void shouldThrowExceptionForNullScore() {
            // Given
            var match = new Match(new Team("Brazil"), new Team("Germany"));

            // When/Then
            var exception = assertThrows(IllegalArgumentException.class,
                    () -> match.updateScore(null));
            assertTrue(exception.getMessage().contains("Score cannot be null"));
        }
    }

    @Nested
    @DisplayName("equality")
    class Equality {
        @Test
        @DisplayName("considers matches with same teams equal")
        void shouldConsiderMatchesWithSameTeamsEqual() {
            // Given
            var homeTeam = new Team("Brazil");
            var awayTeam = new Team("Germany");
            var match1 = new Match(homeTeam, awayTeam);
            var match2 = new Match(homeTeam, awayTeam);
            // Then
            assertEquals(match1, match2);
            assertEquals(match1.hashCode(), match2.hashCode());
        }

        @Test
        @DisplayName("considers matches with different teams not equal")
        void shouldConsiderMatchesWithDifferentTeamsNotEqual() {
            // Given
            var match1 = new Match(new Team("Brazil"), new Team("Germany"));
            var match2 = new Match(new Team("Spain"), new Team("Italy"));
            // Then
            assertNotEquals(match1, match2);
        }

        @Test
        @DisplayName("considers team order important for equality")
        void shouldConsiderTeamOrderImportantForEquality() {
            // Given
            var teamA = new Team("Brazil");
            var teamB = new Team("Germany");
            var match1 = new Match(teamA, teamB);
            var match2 = new Match(teamB, teamA);

            // Then
            assertNotEquals(match1, match2);
            assertNotEquals(match1.hashCode(), match2.hashCode());
        }
    }

    @Test
    @DisplayName("formats match correctly in toString()")
    void shouldFormatToStringCorrectly() {
        // Given
        var match = new Match(new Team("Brazil"), new Team("Germany"));
        match.updateScore(new Score(2, 1));

        // Then
        assertEquals("Brazil 2 - 1 Germany", match.toString());
    }
}