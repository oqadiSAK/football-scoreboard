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

public class ScoreboardServiceImpl implements ScoreboardService {
    private final MatchRepository repository;
    private final Comparator<Match> matchComparator;

    public ScoreboardServiceImpl(MatchRepository repository, Comparator<Match> matchComparator) {
        this.repository = repository;
        this.matchComparator = matchComparator;
    }

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

    @Override
    public void updateScore(String homeTeamName, String awayTeamName, int homeScore, int awayScore) {
        Team homeTeam = new Team(homeTeamName);
        Team awayTeam = new Team(awayTeamName);
        Score newScore = new Score(homeScore, awayScore);

        Match match = findMatchOrThrow(homeTeam, awayTeam);
        match.updateScore(newScore);

        repository.save(match);
    }

    @Override
    public void finishMatch(String homeTeamName, String awayTeamName) {
        Team homeTeam = new Team(homeTeamName);
        Team awayTeam = new Team(awayTeamName);

        Match match = findMatchOrThrow(homeTeam, awayTeam);
        repository.delete(match);
    }

    @Override
    public List<Match> getSummary() {
        return repository.findAll().stream()
                .sorted(matchComparator)
                .collect(Collectors.toList());
    }

    private void checkTeamAvailability(Team team) {
        if (repository.existsByTeam(team)) {
            throw new TeamAlreadyPlayingException(team);
        }
    }

    private Match findMatchOrThrow(Team homeTeam, Team awayTeam) {
        return repository.findByTeams(homeTeam, awayTeam)
                .orElseThrow(() -> new MatchNotFoundException(homeTeam, awayTeam));
    }
}