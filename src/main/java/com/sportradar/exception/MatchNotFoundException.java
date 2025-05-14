package com.sportradar.exception;

import com.sportradar.domain.Team;

public class MatchNotFoundException extends RuntimeException {
    private final Team homeTeam;
    private final Team awayTeam;

    public MatchNotFoundException(Team homeTeam, Team awayTeam) {
        super("Match not found for teams: '" + homeTeam.getName() + "' vs '" + awayTeam.getName() + "'");
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }
}