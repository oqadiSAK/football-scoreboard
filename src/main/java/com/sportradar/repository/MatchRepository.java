package com.sportradar.repository;

import java.util.List;
import java.util.Optional;

import com.sportradar.domain.Match;
import com.sportradar.domain.Team;

/**
 * Repository interface for managing match data.
 * <p>
 * This repository provides methods to save, delete, and retrieve match data.
 * Implementations of this interface determine how and where match data is
 * stored.
 * </p>
 *
 */
public interface MatchRepository {

    /**
     * Saves a match to the repository.
     * <p>
     * If the match already exists (based on home and away teams), it updates the
     * existing match.
     * If the match does not exist, it adds it as a new match.
     * </p>
     *
     * @param match the match to save
     * @throws IllegalArgumentException if the match is null
     */
    void save(Match match);

    /**
     * Removes a match from the repository.
     * <p>
     * If the match does not exist in the repository, this method has no effect.
     * </p>
     *
     * @param match the match to remove
     * @throws IllegalArgumentException if the match is null
     */
    void delete(Match match);

    /**
     * Finds a match by the home and away teams.
     * <p>
     * Searches for a match with the exact home and away teams provided.
     * </p>
     *
     * @param homeTeam the home team
     * @param awayTeam the away team
     * @return an Optional containing the match if found, or empty if not found
     * @throws IllegalArgumentException if either team is null
     */
    Optional<Match> findByTeams(Team homeTeam, Team awayTeam);

    /**
     * Retrieves all matches currently stored in the repository.
     * <p>
     * The returned list is a snapshot of the matches at the time of the call.
     * Changes to this list do not affect the repository.
     * </p>
     *
     * @return a list of all matches
     */
    List<Match> findAll();

    /**
     * Checks if a team is currently playing in any match in the repository.
     * <p>
     * A team is considered to be playing if it appears as either the home team
     * or away team in any match in the repository.
     * </p>
     *
     * @param team the team to check
     * @return true if the team is playing in any match, false otherwise
     * @throws IllegalArgumentException if the team is null
     */
    boolean existsByTeam(Team team);
}