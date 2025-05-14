package com.sportradar.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.sportradar.domain.Match;
import com.sportradar.domain.Score;
import com.sportradar.domain.Team;

@DisplayName("InMemoryMatchRepository")
class InMemoryMatchRepositoryTest {

    private MatchRepository repository;
    private Team homeTeam;
    private Team awayTeam;
    private Match match;

    @BeforeEach
    void setUp() {
        repository = new InMemoryMatchRepository();
        homeTeam = new Team("Germany");
        awayTeam = new Team("Brazil");
        match = new Match(homeTeam, awayTeam);
    }

    @Nested
    @DisplayName("save operation")
    class SaveOperation {
        @Test
        @DisplayName("stores match correctly")
        void shouldStoreMatch() {
            // When
            repository.save(match);

            // Then
            Optional<Match> found = repository.findByTeams(homeTeam, awayTeam);
            assertTrue(found.isPresent());
            assertEquals(match, found.get());
        }

        @Test
        @DisplayName("updates existing match")
        void shouldUpdateExistingMatch() {
            // Given
            repository.save(match);
            match.updateScore(new Score(2, 1));

            // When
            repository.save(match);

            // Then
            Optional<Match> found = repository.findByTeams(homeTeam, awayTeam);
            assertTrue(found.isPresent());
            assertEquals(2, found.get().getHomeScore());
            assertEquals(1, found.get().getAwayScore());
        }
    }

    @Nested
    @DisplayName("delete operation")
    class DeleteOperation {
        @Test
        @DisplayName("removes match")
        void shouldRemoveMatch() {
            // Given
            repository.save(match);

            // When
            repository.delete(match);

            // Then
            Optional<Match> found = repository.findByTeams(homeTeam, awayTeam);
            assertFalse(found.isPresent());
        }

        @Test
        @DisplayName("handles deletion of non-existent match")
        void shouldHandleNonExistentMatchDeletion() {
            // When/Then - should not throw
            assertDoesNotThrow(() -> repository.delete(match));
        }
    }

    @Nested
    @DisplayName("findByTeams operation")
    class FindByTeamsOperation {
        @Test
        @DisplayName("returns match when exists")
        void shouldReturnMatchWhenExists() {
            // Given
            repository.save(match);

            // When
            Optional<Match> found = repository.findByTeams(homeTeam, awayTeam);

            // Then
            assertTrue(found.isPresent());
            assertEquals(match, found.get());
        }

        @Test
        @DisplayName("returns empty when match does not exist")
        void shouldReturnEmptyWhenMatchDoesNotExist() {
            // When
            Optional<Match> found = repository.findByTeams(homeTeam, awayTeam);

            // Then
            assertFalse(found.isPresent());
        }
    }

    @Nested
    @DisplayName("findAll operation")
    class FindAllOperation {
        @Test
        @DisplayName("returns all matches")
        void shouldReturnAllMatches() {
            // Given
            repository.save(match);
            Match anotherMatch = new Match(new Team("Spain"), new Team("Italy"));
            repository.save(anotherMatch);

            // When
            List<Match> matches = repository.findAll();

            // Then
            assertEquals(2, matches.size());
            assertTrue(matches.contains(match));
            assertTrue(matches.contains(anotherMatch));
        }

        @Test
        @DisplayName("returns empty list when no matches")
        void shouldReturnEmptyListWhenNoMatches() {
            // When
            List<Match> matches = repository.findAll();

            // Then
            assertTrue(matches.isEmpty());
        }
    }

    @Nested
    @DisplayName("existsByTeam operation")
    class ExistsByTeamOperation {
        @Test
        @DisplayName("returns true when team is playing")
        void shouldReturnTrueWhenTeamIsPlaying() {
            // Given
            repository.save(match);

            // When/Then
            assertTrue(repository.existsByTeam(homeTeam));
            assertTrue(repository.existsByTeam(awayTeam));
        }

        @Test
        @DisplayName("returns false when team is not playing")
        void shouldReturnFalseWhenTeamIsNotPlaying() {
            // When/Then
            assertFalse(repository.existsByTeam(homeTeam));
            assertFalse(repository.existsByTeam(awayTeam));
        }

        @Test
        @DisplayName("returns false when team was playing but match finished")
        void shouldReturnFalseWhenTeamWasPlayingButMatchFinished() {
            // Given
            repository.save(match);
            repository.delete(match);

            // When/Then
            assertFalse(repository.existsByTeam(homeTeam));
            assertFalse(repository.existsByTeam(awayTeam));
        }

        @Test
        @DisplayName("tracks teams in multiple matches correctly")
        void shouldTrackTeamsInMultipleMatchesCorrectly() {
            // Given
            repository.save(match);
            Team thirdTeam = new Team("France");
            Match anotherMatch = new Match(homeTeam, thirdTeam);
            repository.save(anotherMatch);

            // When
            repository.delete(match);

            // Then
            assertTrue(repository.existsByTeam(homeTeam));
            assertFalse(repository.existsByTeam(awayTeam));
            assertTrue(repository.existsByTeam(thirdTeam));
        }
    }
}