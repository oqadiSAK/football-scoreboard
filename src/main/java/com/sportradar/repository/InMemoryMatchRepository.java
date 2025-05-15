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

/**
 * In-memory implementation of the {@link MatchRepository} interface.
 * <p>
 * This implementation stores matches in memory and provides no persistence
 * across application restarts. It uses a HashMap for efficient lookup
 * of matches by team combinations.
 * </p>
 *
 */
public class InMemoryMatchRepository implements MatchRepository {
    private final Map<MatchKey, Match> matches = new HashMap<>();
    private final Set<Team> teams = new HashSet<>();

    /**
     * {@inheritDoc}
     * 
     * @throws IllegalArgumentException if the match is null
     */
    @Override
    public void save(Match match) {
        if (match == null) {
            throw new IllegalArgumentException("Match cannot be null");
        }
        MatchKey key = new MatchKey(match.getHomeTeam(), match.getAwayTeam());
        matches.put(key, match);
        teams.add(match.getHomeTeam());
        teams.add(match.getAwayTeam());
    }

    /**
     * {@inheritDoc}
     * 
     * @throws IllegalArgumentException if the match is null
     */
    @Override
    public void delete(Match match) {
        if (match == null) {
            throw new IllegalArgumentException("Match cannot be null");
        }
        MatchKey key = new MatchKey(match.getHomeTeam(), match.getAwayTeam());

        if (matches.remove(key) != null) {
            checkAndRemoveTeam(match.getHomeTeam());
            checkAndRemoveTeam(match.getAwayTeam());
        }
    }

    /**
     * Removes a team from the teams set if it is no longer referenced by any match.
     *
     * @param team the team to check and potentially remove
     */
    private void checkAndRemoveTeam(Team team) {
        boolean isReferencedByAnyMatch = matches.values().stream()
                .anyMatch(m -> team.equals(m.getHomeTeam()) || team.equals(m.getAwayTeam()));

        if (!isReferencedByAnyMatch) {
            teams.remove(team);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @throws IllegalArgumentException if either team is null
     */
    @Override
    public Optional<Match> findByTeams(Team homeTeam, Team awayTeam) {
        if (homeTeam == null) {
            throw new IllegalArgumentException("Home team cannot be null");
        }
        if (awayTeam == null) {
            throw new IllegalArgumentException("Away team cannot be null");
        }
        MatchKey key = new MatchKey(homeTeam, awayTeam);
        return Optional.ofNullable(matches.get(key));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Match> findAll() {
        return new ArrayList<>(matches.values());
    }

    /**
     * {@inheritDoc}
     * 
     * @throws IllegalArgumentException if the team is null
     */
    @Override
    public boolean existsByTeam(Team team) {
        if (team == null) {
            throw new IllegalArgumentException("Team cannot be null");
        }
        return teams.contains(team);
    }

    /**
     * Private key class used for efficient match lookup in the HashMap.
     * <p>
     * A MatchKey is a combination of home and away teams. Two MatchKeys are
     * considered equal if they have the same home and away teams.
     * </p>
     */
    private static class MatchKey {
        private final Team homeTeam;
        private final Team awayTeam;

        /**
         * Creates a new MatchKey with the specified home and away teams.
         *
         * @param homeTeam the home team
         * @param awayTeam the away team
         */
        public MatchKey(Team homeTeam, Team awayTeam) {
            this.homeTeam = homeTeam;
            this.awayTeam = awayTeam;
        }

        /**
         * {@inheritDoc}
         * <p>
         * Two MatchKeys are considered equal if they have the same home and away teams.
         * </p>
         */
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

        /**
         * {@inheritDoc}
         * <p>
         * The hash code is computed using the home and away teams.
         * </p>
         */
        @Override
        public int hashCode() {
            return Objects.hash(homeTeam, awayTeam);
        }

        /**
         * {@inheritDoc}
         * <p>
         * Returns a string representation of this key in the format "HomeTeam -
         * AwayTeam".
         * </p>
         */
        @Override
        public String toString() {
            return homeTeam + " - " + awayTeam;
        }
    }
}