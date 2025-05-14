package com.sportradar.exception;

import com.sportradar.domain.Team;

public class TeamAlreadyPlayingException extends RuntimeException {
    private final Team team;

    public TeamAlreadyPlayingException(Team team) {
        super("Team '" + team.getName() + "' is already playing in another match");
        this.team = team;
    }

    public Team getTeam() {
        return team;
    }
}