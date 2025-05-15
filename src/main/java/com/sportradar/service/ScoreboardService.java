package com.sportradar.service;

import java.util.List;

import com.sportradar.domain.Match;
import com.sportradar.exception.MatchNotFoundException;
import com.sportradar.exception.TeamAlreadyPlayingException;

/**
 * Service interface for managing the football scoreboard.
 * <p>
 * This interface provides operations to start matches, update scores,
 * finish matches, and get summaries of ongoing matches.
 * </p>
 *
 */
public interface ScoreboardService {
    /**
     * Starts a new match with the specified teams and initial score 0-0.
     * <p>
     * Creates new team objects for the home and away teams and ensures neither team
     * is already playing in another match.
     * </p>
     *
     * @param homeTeamName the name of the home team
     * @param awayTeamName the name of the away team
     * @return the newly created match
     * @throws TeamAlreadyPlayingException if either team is already playing in
     *                                     another match
     * @throws IllegalArgumentException    if team names are invalid (null or empty)
     */
    Match startMatch(String homeTeamName, String awayTeamName);

    /**
     * Updates the score of a match.
     * <p>
     * This method finds the match with the specified teams and updates its score
     * to the provided values. The scores are absolute values, not incremental.
     * </p>
     *
     * @param homeTeamName the name of the home team
     * @param awayTeamName the name of the away team
     * @param homeScore    the new score for the home team
     * @param awayScore    the new score for the away team
     * @throws MatchNotFoundException   if the match is not found
     * @throws IllegalArgumentException if scores are invalid (negative)
     */
    void updateScore(String homeTeamName, String awayTeamName, int homeScore, int awayScore);

    /**
     * Finishes a match, removing it from the scoreboard.
     * <p>
     * This method finds the match with the specified teams and removes it
     * from the scoreboard, indicating that the match has ended.
     * </p>
     *
     * @param homeTeamName the name of the home team
     * @param awayTeamName the name of the away team
     * @throws MatchNotFoundException if the match is not found
     */
    void finishMatch(String homeTeamName, String awayTeamName);

    /**
     * Gets a summary of matches in progress ordered according to the service's
     * configured sorting criteria.
     * <p>
     * The default implementation orders matches by:
     * </p>
     * <ol>
     * <li>Total score (descending) - matches with more total goals come first</li>
     * <li>Start time (most recent first) - for matches with equal total score</li>
     * </ol>
     *
     * @return a list of matches sorted according to the service's configuration
     */
    List<Match> getSummary();
}