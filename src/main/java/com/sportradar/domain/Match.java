package com.sportradar.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Match {
    private final Team homeTeam;
    private final Team awayTeam;
    private Score score;
    private final LocalDateTime startTime;

    public Match(Team homeTeam, Team awayTeam) {
        if (homeTeam == null) {
            throw new IllegalArgumentException("Home team cannot be null");
        }
        if (awayTeam == null) {
            throw new IllegalArgumentException("Away team cannot be null");
        }
        if (homeTeam.equals(awayTeam)) {
            throw new IllegalArgumentException("Home team and away team cannot be the same");
        }
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.score = Score.initial();
        this.startTime = LocalDateTime.now();
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public int getHomeScore() {
        return score.getHomeScore();
    }

    public int getAwayScore() {
        return score.getAwayScore();
    }

    public int getTotalScore() {
        return score.getTotal();
    }

    public Score getScore() {
        return score;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void updateScore(Score newScore) {
        if (newScore == null) {
            throw new IllegalArgumentException("Score cannot be null");
        }
        this.score = newScore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Match match = (Match) o;
        return Objects.equals(homeTeam, match.homeTeam) &&
                Objects.equals(awayTeam, match.awayTeam);
    }

    @Override
    public int hashCode() {
        return Objects.hash(homeTeam, awayTeam);
    }

    @Override
    public String toString() {
        return homeTeam + " " + score.getHomeScore() + " - " +
                score.getAwayScore() + " " + awayTeam;
    }
}