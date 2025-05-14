package com.sportradar.domain;

import java.util.Objects;

public final class Score {
    private final int homeScore;
    private final int awayScore;

    public Score(int homeScore, int awayScore) {
        if (homeScore < 0 || awayScore < 0) {
            throw new IllegalArgumentException("Scores cannot be negative");
        }
        this.homeScore = homeScore;
        this.awayScore = awayScore;
    }

    public static Score initial() {
        return new Score(0, 0);
    }

    public int getHomeScore() {
        return homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public int getTotal() {
        return homeScore + awayScore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Score score = (Score) o;
        return homeScore == score.homeScore && awayScore == score.awayScore;
    }

    @Override
    public int hashCode() {
        return Objects.hash(homeScore, awayScore);
    }

    @Override
    public String toString() {
        return homeScore + " - " + awayScore;
    }
}