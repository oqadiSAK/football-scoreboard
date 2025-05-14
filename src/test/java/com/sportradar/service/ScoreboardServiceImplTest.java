package com.sportradar.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sportradar.domain.Match;
import com.sportradar.domain.Team;
import com.sportradar.exception.MatchNotFoundException;
import com.sportradar.exception.TeamAlreadyPlayingException;
import com.sportradar.repository.MatchRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("ScoreboardServiceImpl")
class ScoreboardServiceImplTest {

    @Mock
    private MatchRepository repository;

    private ScoreboardService scoreboard;
    private Comparator<Match> matchComparator;

    @BeforeEach
    void setUp() {
        matchComparator = Comparator.comparing(Match::getTotalScore, Comparator.reverseOrder())
                .thenComparing(Match::getStartTime, Comparator.reverseOrder());
        scoreboard = new ScoreboardServiceImpl(repository, matchComparator);
    }

    @Nested
    @DisplayName("startMatch operation")
    class StartMatchOperation {
        @Test
        @DisplayName("creates and saves match with valid teams")
        void shouldCreateAndSaveMatchWithValidTeams() {
            // Given
            String homeTeamName = "Germany";
            String awayTeamName = "Brazil";
            Team homeTeam = new Team(homeTeamName);
            Team awayTeam = new Team(awayTeamName);

            when(repository.existsByTeam(any(Team.class))).thenReturn(false);

            // When
            Match match = scoreboard.startMatch(homeTeamName, awayTeamName);

            // Then
            assertAll(
                    () -> assertEquals(homeTeamName, match.getHomeTeam().getName()),
                    () -> assertEquals(awayTeamName, match.getAwayTeam().getName()),
                    () -> assertEquals(0, match.getHomeScore()),
                    () -> assertEquals(0, match.getAwayScore()));

            verify(repository).existsByTeam(homeTeam);
            verify(repository).existsByTeam(awayTeam);

            ArgumentCaptor<Match> matchCaptor = ArgumentCaptor.forClass(Match.class);
            verify(repository).save(matchCaptor.capture());

            Match savedMatch = matchCaptor.getValue();
            assertEquals(homeTeamName, savedMatch.getHomeTeam().getName());
            assertEquals(awayTeamName, savedMatch.getAwayTeam().getName());
        }

        @Test
        @DisplayName("throws exception when home team is already playing")
        void shouldThrowExceptionWhenHomeTeamIsAlreadyPlaying() {
            // Given
            String homeTeamName = "Germany";
            String awayTeamName = "Brazil";
            Team homeTeam = new Team(homeTeamName);

            when(repository.existsByTeam(homeTeam)).thenReturn(true);

            // When/Then
            TeamAlreadyPlayingException exception = assertThrows(TeamAlreadyPlayingException.class,
                    () -> scoreboard.startMatch(homeTeamName, awayTeamName));

            assertEquals(homeTeam, exception.getTeam());
            verify(repository).existsByTeam(homeTeam);
            verify(repository, never()).save(any(Match.class));
        }

        @Test
        @DisplayName("throws exception when away team is already playing")
        void shouldThrowExceptionWhenAwayTeamIsAlreadyPlaying() {
            // Given
            String homeTeamName = "Germany";
            String awayTeamName = "Brazil";
            Team homeTeam = new Team(homeTeamName);
            Team awayTeam = new Team(awayTeamName);

            when(repository.existsByTeam(homeTeam)).thenReturn(false);
            when(repository.existsByTeam(awayTeam)).thenReturn(true);

            // When/Then
            TeamAlreadyPlayingException exception = assertThrows(TeamAlreadyPlayingException.class,
                    () -> scoreboard.startMatch(homeTeamName, awayTeamName));

            assertEquals(awayTeam, exception.getTeam());
            verify(repository).existsByTeam(homeTeam);
            verify(repository).existsByTeam(awayTeam);
            verify(repository, never()).save(any(Match.class));
        }
    }

    @Nested
    @DisplayName("updateScore operation")
    class UpdateScoreOperation {
        @Test
        @DisplayName("updates score for existing match")
        void shouldUpdateScoreForExistingMatch() {
            // Given
            String homeTeamName = "Germany";
            String awayTeamName = "Brazil";
            Team homeTeam = new Team(homeTeamName);
            Team awayTeam = new Team(awayTeamName);
            Match match = new Match(homeTeam, awayTeam);

            when(repository.findByTeams(homeTeam, awayTeam)).thenReturn(Optional.of(match));

            // When
            scoreboard.updateScore(homeTeamName, awayTeamName, 2, 3);

            // Then
            assertEquals(2, match.getHomeScore());
            assertEquals(3, match.getAwayScore());

            verify(repository).findByTeams(homeTeam, awayTeam);
            verify(repository).save(match);
        }

        @Test
        @DisplayName("throws exception when match does not exist")
        void shouldThrowExceptionWhenMatchDoesNotExist() {
            // Given
            String homeTeamName = "Germany";
            String awayTeamName = "Brazil";
            Team homeTeam = new Team(homeTeamName);
            Team awayTeam = new Team(awayTeamName);

            when(repository.findByTeams(homeTeam, awayTeam)).thenReturn(Optional.empty());

            // When/Then
            MatchNotFoundException exception = assertThrows(MatchNotFoundException.class,
                    () -> scoreboard.updateScore(homeTeamName, awayTeamName, 2, 3));

            assertEquals(homeTeam, exception.getHomeTeam());
            assertEquals(awayTeam, exception.getAwayTeam());

            verify(repository).findByTeams(homeTeam, awayTeam);
            verify(repository, never()).save(any(Match.class));
        }
    }

    @Nested
    @DisplayName("finishMatch operation")
    class FinishMatchOperation {
        @Test
        @DisplayName("deletes existing match")
        void shouldDeleteExistingMatch() {
            // Given
            String homeTeamName = "Germany";
            String awayTeamName = "Brazil";
            Team homeTeam = new Team(homeTeamName);
            Team awayTeam = new Team(awayTeamName);
            Match match = new Match(homeTeam, awayTeam);

            when(repository.findByTeams(homeTeam, awayTeam)).thenReturn(Optional.of(match));

            // When
            scoreboard.finishMatch(homeTeamName, awayTeamName);

            // Then
            verify(repository).findByTeams(homeTeam, awayTeam);
            verify(repository).delete(match);
        }

        @Test
        @DisplayName("throws exception when match does not exist")
        void shouldThrowExceptionWhenMatchDoesNotExist() {
            // Given
            String homeTeamName = "Germany";
            String awayTeamName = "Brazil";
            Team homeTeam = new Team(homeTeamName);
            Team awayTeam = new Team(awayTeamName);

            when(repository.findByTeams(homeTeam, awayTeam)).thenReturn(Optional.empty());

            // When/Then
            MatchNotFoundException exception = assertThrows(MatchNotFoundException.class,
                    () -> scoreboard.finishMatch(homeTeamName, awayTeamName));

            assertEquals(homeTeam, exception.getHomeTeam());
            assertEquals(awayTeam, exception.getAwayTeam());

            verify(repository).findByTeams(homeTeam, awayTeam);
            verify(repository, never()).delete(any(Match.class));
        }
    }

    @Nested
    @DisplayName("getSummary operation")
    class GetSummaryOperation {
        @Test
        @DisplayName("returns matches sorted by total score and start time")
        void shouldReturnMatchesSortedByTotalScoreAndStartTime() {
            // Given
            Match match1 = mock(Match.class);
            Match match2 = mock(Match.class);
            Match match3 = mock(Match.class);
            Match match4 = mock(Match.class);
            Match match5 = mock(Match.class);

            lenient().when(match1.getTotalScore()).thenReturn(4);
            lenient().when(match2.getTotalScore()).thenReturn(5);
            lenient().when(match3.getTotalScore()).thenReturn(12);
            lenient().when(match4.getTotalScore()).thenReturn(12);
            lenient().when(match5.getTotalScore()).thenReturn(4);

            LocalDateTime baseTime = LocalDateTime.now();
            lenient().when(match1.getStartTime()).thenReturn(baseTime.minusMinutes(5));
            lenient().when(match3.getStartTime()).thenReturn(baseTime.minusMinutes(4));
            lenient().when(match2.getStartTime()).thenReturn(baseTime.minusMinutes(3));
            lenient().when(match4.getStartTime()).thenReturn(baseTime.minusMinutes(2));
            lenient().when(match5.getStartTime()).thenReturn(baseTime.minusMinutes(1));

            lenient().when(match1.getHomeTeam()).thenReturn(new Team("Germany"));
            lenient().when(match1.getAwayTeam()).thenReturn(new Team("France"));
            lenient().when(match1.getHomeScore()).thenReturn(2);
            lenient().when(match1.getAwayScore()).thenReturn(2);

            lenient().when(match2.getHomeTeam()).thenReturn(new Team("Mexico"));
            lenient().when(match2.getAwayTeam()).thenReturn(new Team("Canada"));
            lenient().when(match2.getHomeScore()).thenReturn(0);
            lenient().when(match2.getAwayScore()).thenReturn(5);

            lenient().when(match3.getHomeTeam()).thenReturn(new Team("Spain"));
            lenient().when(match3.getAwayTeam()).thenReturn(new Team("Brazil"));
            lenient().when(match3.getHomeScore()).thenReturn(10);
            lenient().when(match3.getAwayScore()).thenReturn(2);

            lenient().when(match4.getHomeTeam()).thenReturn(new Team("Uruguay"));
            lenient().when(match4.getAwayTeam()).thenReturn(new Team("Italy"));
            lenient().when(match4.getHomeScore()).thenReturn(6);
            lenient().when(match4.getAwayScore()).thenReturn(6);

            lenient().when(match5.getHomeTeam()).thenReturn(new Team("Argentina"));
            lenient().when(match5.getAwayTeam()).thenReturn(new Team("Australia"));
            lenient().when(match5.getHomeScore()).thenReturn(3);
            lenient().when(match5.getAwayScore()).thenReturn(1);

            when(repository.findAll()).thenReturn(Arrays.asList(match1, match2, match3, match4, match5));

            // When
            List<Match> summary = scoreboard.getSummary();

            // Then
            assertEquals(5, summary.size());

            assertSame(match4, summary.get(0));
            assertSame(match3, summary.get(1));
            assertSame(match2, summary.get(2));
            assertSame(match5, summary.get(3));
            assertSame(match1, summary.get(4));

            verify(repository).findAll();
        }

        @Test
        @DisplayName("returns empty list when no matches exist")
        void shouldReturnEmptyListWhenNoMatchesExist() {
            // Given
            when(repository.findAll()).thenReturn(List.of());

            // When
            List<Match> summary = scoreboard.getSummary();

            // Then
            assertTrue(summary.isEmpty());
            verify(repository).findAll();
        }
    }
}