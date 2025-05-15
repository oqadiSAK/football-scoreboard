package com.sportradar.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.sportradar.domain.Match;
import com.sportradar.domain.Score;
import com.sportradar.domain.Team;
import com.sportradar.exception.MatchNotFoundException;
import com.sportradar.exception.TeamAlreadyPlayingException;
import com.sportradar.repository.MatchRepository;

/**
 * Default implementation of the {@link ScoreboardService} interface.
 * <p>
 * This implementation uses a {@link MatchRepository} for data storage and
 * a custom comparator for sorting matches in the summary.
 * </p>
 *
 * @see ScoreboardService
 * @see MatchRepository
 */
public class ScoreboardServiceImpl implements ScoreboardService {
    private final MatchRepository repository;
    private final Comparator<Match> matchComparator;

    /**
     * Creates a new ScoreboardServiceImpl with the specified repository and match
     * comparator.
     *
     * @param repository      the repository to use for data storage
     * @param matchComparator the comparator to use for sorting matches in the
     *                        summary
     * @throws IllegalArgumentException if either parameter is null
     */
    public ScoreboardServiceImpl(MatchRepository repository, Comparator<Match> matchComparator) {
        if (repository == null) {
            throw new IllegalArgumentException("Repository cannot be null");
        }
        if (matchComparator == null) {
            throw new IllegalArgumentException("Match comparator cannot be null");
        }
        this.repository = repository;
        this.matchComparator = matchComparator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Match startMatch(String homeTeamName, String awayTeamName) {
        Team homeTeam = new Team(homeTeamName);
        Team awayTeam = new Team(awayTeamName);

        checkTeamAvailability(homeTeam);
        checkTeamAvailability(awayTeam);

        Match match = new Match(homeTeam, awayTeam);
        repository.save(match);
        return match;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateScore(String homeTeamName, String awayTeamName, int homeScore, int awayScore) {
        Team homeTeam = new Team(homeTeamName);
        Team awayTeam = new Team(awayTeamName);
        Score newScore = new Score(homeScore, awayScore);

        Match match = findMatchOrThrow(homeTeam, awayTeam);
        match.updateScore(newScore);

        repository.save(match);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void finishMatch(String homeTeamName, String awayTeamName) {
        Team homeTeam = new Team(homeTeamName);
        Team awayTeam = new Team(awayTeamName);

        Match match = findMatchOrThrow(homeTeam, awayTeam);
        repository.delete(match);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Match> getSummary() {
        return repository.findAll().stream()
                .sorted(matchComparator)
                .collect(Collectors.toList());
    }

    /**
     * Checks if a team is available to play in a new match.
     * <p>
     * A team is considered available if it is not already playing in another match.
     * </p>
     *
     * @param team the team to check
     * @throws TeamAlreadyPlayingException if the team is already playing in another
     *                                     match
     */
    private void checkTeamAvailability(Team team) {
        if (repository.existsByTeam(team)) {
            throw new TeamAlreadyPlayingException(team);
        }
    }

    /**
     * Finds a match by home and away teams, throwing an exception if not found.
     *
     * @param homeTeam the home team
     * @param awayTeam the away team
     * @return the match if found
     * @throws MatchNotFoundException if no match is found for the specified teams
     */
    private Match findMatchOrThrow(Team homeTeam, Team awayTeam) {
        return repository.findByTeams(homeTeam, awayTeam)
                .orElseThrow(() -> new MatchNotFoundException(homeTeam, awayTeam));
    }
}