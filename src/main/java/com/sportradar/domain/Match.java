package com.sportradar.domain;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a football match with two teams and a current score.
 * <p>
 * A match contains the home and away teams, the current score, and the start
 * time.
 * Match objects are uniquely identified by their home and away team
 * combination.
 * Once created, the teams cannot be changed, but the score can be updated.
 * </p>
 * 
 */
public class Match {
    private final Team homeTeam;
    private final Team awayTeam;
    private Score score;
    private final LocalDateTime startTime;

    /**
     * Creates a new match with the specified home and away teams.
     * <p>
     * The initial score is set to 0-0, and the start time is set to the current
     * time.
     * </p>
     * 
     * @param homeTeam the home team
     * @param awayTeam the away team
     * @throws IllegalArgumentException if either team is null or if the home and
     *                                  away teams are the same
     */
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

    /**
     * Returns the home team of this match.
     *
     * @return the home team
     */
    public Team getHomeTeam() {
        return homeTeam;
    }

    /**
     * Returns the away team of this match.
     *
     * @return the away team
     */
    public Team getAwayTeam() {
        return awayTeam;
    }

    /**
     * Returns the current score of the home team.
     *
     * @return the home team's score
     */
    public int getHomeScore() {
        return score.getHomeScore();
    }

    /**
     * Returns the current score of the away team.
     *
     * @return the away team's score
     */
    public int getAwayScore() {
        return score.getAwayScore();
    }

    /**
     * Returns the total combined score of both teams.
     *
     * @return the sum of the home and away scores
     */
    public int getTotalScore() {
        return score.getTotal();
    }

    /**
     * Returns the current score object.
     *
     * @return the current score
     */
    public Score getScore() {
        return score;
    }

    /**
     * Returns the start time of this match.
     *
     * @return the time when the match was created
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * Updates the score of this match.
     * <p>
     * This method replaces the current score with the provided score.
     * </p>
     *
     * @param newScore the new score
     * @throws IllegalArgumentException if the score is null
     */
    public void updateScore(Score newScore) {
        if (newScore == null) {
            throw new IllegalArgumentException("Score cannot be null");
        }
        this.score = newScore;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Two matches are considered equal if they have the same home and away teams.
     * </p>
     */
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
     * Returns a string representation of this match in the format:
     * "HomeTeam HomeScore - AwayScore AwayTeam"
     * </p>
     */
    @Override
    public String toString() {
        return homeTeam + " " + score.getHomeScore() + " - " +
                score.getAwayScore() + " " + awayTeam;
    }
}