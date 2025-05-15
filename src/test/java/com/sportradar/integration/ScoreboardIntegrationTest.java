package com.sportradar.integration;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.sportradar.domain.Match;
import com.sportradar.exception.MatchNotFoundException;
import com.sportradar.exception.TeamAlreadyPlayingException;
import com.sportradar.factory.ScoreboardServiceFactory;
import com.sportradar.service.ScoreboardService;

@DisplayName("Scoreboard Integration")
class ScoreboardIntegrationTest {

    private ScoreboardService scoreboard;

    @BeforeEach
    void setUp() {
        scoreboard = ScoreboardServiceFactory.createDefault();
    }

    @Test
    @DisplayName("follows the example scenario from requirements")
    void shouldMatchExampleScenario() {
        // Start matches
        scoreboard.startMatch("Mexico", "Canada");
        scoreboard.startMatch("Spain", "Brazil");
        scoreboard.startMatch("Germany", "France");
        scoreboard.startMatch("Uruguay", "Italy");
        scoreboard.startMatch("Argentina", "Australia");

        // Update scores
        scoreboard.updateScore("Mexico", "Canada", 0, 5);
        scoreboard.updateScore("Spain", "Brazil", 10, 2);
        scoreboard.updateScore("Germany", "France", 2, 2);
        scoreboard.updateScore("Uruguay", "Italy", 6, 6);
        scoreboard.updateScore("Argentina", "Australia", 3, 1);

        // Get summary
        List<Match> summary = scoreboard.getSummary();

        // Assert
        assertEquals(5, summary.size());

        // 1. Uruguay 6 - Italy 6 (Total: 12, most recent with highest score)
        assertMatchEquals(summary.get(0), "Uruguay", "Italy", 6, 6);

        // 2. Spain 10 - Brazil 2 (Total: 12, older with highest score)
        assertMatchEquals(summary.get(1), "Spain", "Brazil", 10, 2);

        // 3. Mexico 0 - Canada 5 (Total: 5)
        assertMatchEquals(summary.get(2), "Mexico", "Canada", 0, 5);

        // 4. Argentina 3 - Australia 1 (Total: 4, most recent)
        assertMatchEquals(summary.get(3), "Argentina", "Australia", 3, 1);

        // 5. Germany 2 - France 2 (Total: 4, oldest)
        assertMatchEquals(summary.get(4), "Germany", "France", 2, 2);
    }

    @Nested
    @DisplayName("team availability")
    class TeamAvailability {
        @Test
        @DisplayName("prevents team from playing in multiple matches")
        void shouldPreventTeamFromPlayingInMultipleMatches() {
            // Given
            scoreboard.startMatch("Brazil", "Germany");

            // When/Then
            assertThrows(TeamAlreadyPlayingException.class, () -> scoreboard.startMatch("Brazil", "Italy"));

            assertThrows(TeamAlreadyPlayingException.class, () -> scoreboard.startMatch("Spain", "Brazil"));
        }

        @Test
        @DisplayName("allows team to play again after match is finished")
        void shouldAllowTeamToPlayAgainAfterMatchIsFinished() {
            // Given
            scoreboard.startMatch("Brazil", "Germany");
            scoreboard.finishMatch("Brazil", "Germany");

            // When/Then
            assertDoesNotThrow(() -> scoreboard.startMatch("Brazil", "Italy"));
        }
    }

    @Nested
    @DisplayName("match operations")
    class MatchOperations {
        @Test
        @DisplayName("updates score correctly")
        void shouldUpdateScoreCorrectly() {
            // Given
            scoreboard.startMatch("Brazil", "Germany");

            // When
            scoreboard.updateScore("Brazil", "Germany", 3, 2);

            // Then
            List<Match> matches = scoreboard.getSummary();
            assertEquals(1, matches.size());
            assertMatchEquals(matches.get(0), "Brazil", "Germany", 3, 2);
        }

        @Test
        @DisplayName("removes match after finish")
        void shouldRemoveMatchAfterFinish() {
            // Given
            scoreboard.startMatch("Brazil", "Germany");
            scoreboard.startMatch("Spain", "Italy");

            // When
            scoreboard.finishMatch("Brazil", "Germany");

            // Then
            List<Match> matches = scoreboard.getSummary();
            assertEquals(1, matches.size());
            assertMatchEquals(matches.get(0), "Spain", "Italy", 0, 0);
        }

        @Test
        @DisplayName("throws exception when updating non-existent match")
        void shouldThrowExceptionWhenUpdatingNonExistentMatch() {
            // When/Then
            assertThrows(MatchNotFoundException.class, () -> scoreboard.updateScore("Brazil", "Germany", 3, 2));
        }

        @Test
        @DisplayName("throws exception when finishing non-existent match")
        void shouldThrowExceptionWhenFinishingNonExistentMatch() {
            // When/Then
            assertThrows(MatchNotFoundException.class, () -> scoreboard.finishMatch("Brazil", "Germany"));
        }
    }

    // Helper method to assert match details
    private void assertMatchEquals(Match match, String homeTeamName, String awayTeamName,
            int homeScore, int awayScore) {
        assertEquals(homeTeamName, match.getHomeTeam().getName());
        assertEquals(awayTeamName, match.getAwayTeam().getName());
        assertEquals(homeScore, match.getHomeScore());
        assertEquals(awayScore, match.getAwayScore());
    }
}