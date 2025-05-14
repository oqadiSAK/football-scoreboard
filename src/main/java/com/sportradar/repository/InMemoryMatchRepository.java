package com.sportradar.repository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import com.sportradar.domain.Match;
import com.sportradar.domain.Team;

import java.util.List;
import java.util.ArrayList;

public class InMemoryMatchRepository implements MatchRepository {
    private final Map<MatchKey, Match> matches = new HashMap<>();
    private final Set<Team> teams = new HashSet<>();

    @Override
    public void save(Match match) {
        MatchKey key = new MatchKey(match.getHomeTeam(), match.getAwayTeam());
        matches.put(key, match);
        teams.add(match.getHomeTeam());
        teams.add(match.getAwayTeam());
    }

    @Override
    public void delete(Match match) {
        MatchKey key = new MatchKey(match.getHomeTeam(), match.getAwayTeam());

        if (matches.remove(key) != null) {
            checkAndRemoveTeam(match.getHomeTeam());
            checkAndRemoveTeam(match.getAwayTeam());
        }
    }

    private void checkAndRemoveTeam(Team team) {
        boolean isReferencedByAnyMatch = matches.values().stream()
                .anyMatch(m -> team.equals(m.getHomeTeam()) || team.equals(m.getAwayTeam()));

        if (!isReferencedByAnyMatch) {
            teams.remove(team);
        }
    }

    @Override
    public Optional<Match> findByTeams(Team homeTeam, Team awayTeam) {
        MatchKey key = new MatchKey(homeTeam, awayTeam);
        return Optional.ofNullable(matches.get(key));
    }

    @Override
    public List<Match> findAll() {
        return new ArrayList<>(matches.values());
    }

    @Override
    public boolean existsByTeam(Team team) {
        return teams.contains(team);
    }

    private static class MatchKey {
        private final Team homeTeam;
        private final Team awayTeam;

        public MatchKey(Team homeTeam, Team awayTeam) {
            this.homeTeam = homeTeam;
            this.awayTeam = awayTeam;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            MatchKey matchKey = (MatchKey) o;
            return Objects.equals(homeTeam, matchKey.homeTeam) &&
                    Objects.equals(awayTeam, matchKey.awayTeam);
        }

        @Override
        public int hashCode() {
            return Objects.hash(homeTeam, awayTeam);
        }

        @Override
        public String toString() {
            return homeTeam + " - " + awayTeam;
        }
    }
}