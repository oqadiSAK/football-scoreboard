package com.sportradar.exception;

import com.sportradar.domain.Team;

/**
 * Exception thrown when a match is not found in the repository.
 * <p>
 * This exception is thrown when attempting to update or finish a match
 * that does not exist in the repository.
 * </p>
 *
 */
public class MatchNotFoundException extends RuntimeException {
    private final Team homeTeam;
    private final Team awayTeam;

    /**
     * Constructs a new exception with the specified home and away teams.
     *
     * @param homeTeam the home team of the match that was not found
     * @param awayTeam the away team of the match that was not found
     */
    public MatchNotFoundException(Team homeTeam, Team awayTeam) {
        super("Match not found for teams: '" + homeTeam.getName() + "' vs '" + awayTeam.getName() + "'");
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
    }

    /**
     * Returns the home team of the match that was not found.
     *
     * @return the home team
     */
    public Team getHomeTeam() {
        return homeTeam;
    }

    /**
     * Returns the away team of the match that was not found.
     *
     * @return the away team
     */
    public Team getAwayTeam() {
        return awayTeam;
    }
}