package com.sportradar.exception;

import com.sportradar.domain.Team;

/**
 * Exception thrown when attempting to start a match with a team that is already
 * playing.
 * <p>
 * This exception is thrown when attempting to start a new match with a team
 * that
 * is already participating in another ongoing match.
 * </p>
 *
 */
public class TeamAlreadyPlayingException extends RuntimeException {
    private final Team team;

    /**
     * Constructs a new exception with the specified team.
     *
     * @param team the team that is already playing in another match
     */
    public TeamAlreadyPlayingException(Team team) {
        super("Team '" + team.getName() + "' is already playing in another match");
        this.team = team;
    }

    /**
     * Returns the team that is already playing in another match.
     *
     * @return the team that caused the exception
     */
    public Team getTeam() {
        return team;
    }
}