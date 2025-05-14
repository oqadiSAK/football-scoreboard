package com.sportradar.service;

import java.util.List;

import com.sportradar.domain.Match;
import com.sportradar.exception.MatchNotFoundException;
import com.sportradar.exception.TeamAlreadyPlayingException;

public interface ScoreboardService {
    /**
     * Starts a new match with the specified teams and initial score 0-0.
     *
     * @param homeTeamName the name of the home team
     * @param awayTeamName the name of the away team
     * @return the newly created match
     * @throws TeamAlreadyPlayingException if either team is already playing in
     *                                     another match
     * @throws IllegalArgumentException    if team names are invalid
     */
    Match startMatch(String homeTeamName, String awayTeamName);

    /**
     * Updates the score of a match.
     *
     * @param homeTeamName the name of the home team
     * @param awayTeamName the name of the away team
     * @param homeScore    the new score for the home team
     * @param awayScore    the new score for the away team
     * @throws MatchNotFoundException   if the match is not found
     * @throws IllegalArgumentException if scores are invalid
     */
    void updateScore(String homeTeamName, String awayTeamName, int homeScore, int awayScore);

    /**
     * Finishes a match, removing it from the scoreboard.
     *
     * @param homeTeamName the name of the home team
     * @param awayTeamName the name of the away team
     * @throws MatchNotFoundException if the match is not found
     */
    void finishMatch(String homeTeamName, String awayTeamName);

    /**
     * Gets a summary of matches in progress ordered by total score (descending).
     * For matches with equal total score, they are ordered by most recently started
     * match first.
     *
     * @return a list of matches sorted by total score and start time
     */
    List<Match> getSummary();
}