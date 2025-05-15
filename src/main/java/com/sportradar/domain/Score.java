package com.sportradar.domain;

import java.util.Objects;

/**
 * Represents the score of a football match.
 * <p>
 * A score consists of the number of goals scored by the home team and the away
 * team.
 * Score objects are immutable - once created, they cannot be changed.
 * </p>
 *
 */
public final class Score {
    private final int homeScore;
    private final int awayScore;

    /**
     * Creates a new score with the specified home and away scores.
     *
     * @param homeScore the number of goals scored by the home team
     * @param awayScore the number of goals scored by the away team
     * @throws IllegalArgumentException if either score is negative
     */
    public Score(int homeScore, int awayScore) {
        if (homeScore < 0 || awayScore < 0) {
            throw new IllegalArgumentException("Scores cannot be negative");
        }
        this.homeScore = homeScore;
        this.awayScore = awayScore;
    }

    /**
     * Creates a new score with the initial value of 0-0.
     *
     * @return a new score with both home and away scores set to 0
     */
    public static Score initial() {
        return new Score(0, 0);
    }

    /**
     * Returns the number of goals scored by the home team.
     *
     * @return the home team's score
     */
    public int getHomeScore() {
        return homeScore;
    }

    /**
     * Returns the number of goals scored by the away team.
     *
     * @return the away team's score
     */
    public int getAwayScore() {
        return awayScore;
    }

    /**
     * Returns the total combined score of both teams.
     *
     * @return the sum of the home and away scores
     */
    public int getTotal() {
        return homeScore + awayScore;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Two scores are considered equal if they have the same home and away scores.
     * </p>
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Score score = (Score) o;
        return homeScore == score.homeScore && awayScore == score.awayScore;
    }

    /**
     * {@inheritDoc}
     * <p>
     * The hash code is computed using the home and away scores.
     * </p>
     */
    @Override
    public int hashCode() {
        return Objects.hash(homeScore, awayScore);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Returns a string representation of this score in the format "HomeScore -
     * AwayScore".
     * </p>
     */
    @Override
    public String toString() {
        return homeScore + " - " + awayScore;
    }
}