package com.sportradar.factory;

import java.util.Comparator;

import com.sportradar.domain.Match;
import com.sportradar.repository.InMemoryMatchRepository;
import com.sportradar.repository.MatchRepository;
import com.sportradar.service.ScoreboardService;
import com.sportradar.service.ScoreboardServiceImpl;

public class ScoreboardServiceFactory {
    /**
     * Standard comparator that sorts matches by total score (descending)
     * and then by start time (most recent first)
     */
    public static final Comparator<Match> TOTAL_SCORE_AND_TIME_COMPARATOR = Comparator
            .comparing(Match::getTotalScore, Comparator.reverseOrder())
            .thenComparing(Match::getStartTime, Comparator.reverseOrder());

    /**
     * Creates a default scoreboard with the standard sorting
     * (total score descending, then most recent first)
     * 
     * @return a new ScoreboardService instance
     */
    public static ScoreboardService createDefault() {
        MatchRepository repository = new InMemoryMatchRepository();
        return new ScoreboardServiceImpl(repository, TOTAL_SCORE_AND_TIME_COMPARATOR);
    }

    /**
     * Creates a scoreboard with a custom comparator for match sorting
     * 
     * @param matchComparator the comparator to use for sorting matches in
     *                        getSummary()
     * @return a new ScoreboardService instance with the specified sorting
     */
    public static ScoreboardService create(Comparator<Match> matchComparator) {
        MatchRepository repository = new InMemoryMatchRepository();
        return new ScoreboardServiceImpl(repository, matchComparator);
    }
}