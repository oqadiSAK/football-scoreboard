package com.sportradar.repository;

import java.util.List;
import java.util.Optional;

import com.sportradar.domain.Match;
import com.sportradar.domain.Team;

public interface MatchRepository {

    /**
     * Saves a match to the repository.
     * If the match already exists, it updates the existing match.
     *
     * @param match the match to save
     */
    void save(Match match);

    /**
     * Removes a match from the repository.
     *
     * @param match the match to remove
     */
    void delete(Match match);

    /**
     * Finds a match by the home and away teams.
     *
     * @param homeTeam the home team
     * @param awayTeam the away team
     * @return an Optional containing the match if found, or empty if not found
     */
    Optional<Match> findByTeams(Team homeTeam, Team awayTeam);

    /**
     * Retrieves all matches currently stored in the repository.
     *
     * @return a list of all matches
     */
    List<Match> findAll();

    /**
     * Checks if a team is currently playing in any match.
     *
     * @param team the team to check
     * @return true if the team is playing, false otherwise
     */
    boolean existsByTeam(Team team);
}