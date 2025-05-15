package com.sportradar.factory;

import java.util.Comparator;

import com.sportradar.domain.Match;
import com.sportradar.repository.InMemoryMatchRepository;
import com.sportradar.repository.MatchRepository;
import com.sportradar.service.ScoreboardService;
import com.sportradar.service.ScoreboardServiceImpl;

/**
 * Factory class for creating instances of the {@link ScoreboardService}.
 * <p>
 * This factory provides methods to create default or custom configured
 * scoreboard services.
 * </p>
 *
 */
public class ScoreboardServiceFactory {
    /**
     * Standard comparator that sorts matches by total score (descending)
     * and then by start time (most recent first).
     * <p>
     * This comparator is used by the default scoreboard service.
     * </p>
     */
    public static final Comparator<Match> TOTAL_SCORE_AND_TIME_COMPARATOR = Comparator
            .comparing(Match::getTotalScore, Comparator.reverseOrder())
            .thenComparing(Match::getStartTime, Comparator.reverseOrder());

    /**
     * Creates a default scoreboard service with the standard sorting
     * (total score descending, then most recent first).
     * <p>
     * The default service uses an in-memory repository and the
     * {@link #TOTAL_SCORE_AND_TIME_COMPARATOR} for sorting matches.
     * </p>
     * 
     * @return a new ScoreboardService instance
     */
    public static ScoreboardService createDefault() {
        MatchRepository repository = new InMemoryMatchRepository();
        return new ScoreboardServiceImpl(repository, TOTAL_SCORE_AND_TIME_COMPARATOR);
    }

    /**
     * Creates a scoreboard service with a custom comparator for match sorting.
     * <p>
     * This method allows for customizing how matches are sorted in the
     * {@link ScoreboardService#getSummary()} method.
     * </p>
     * 
     * @param matchComparator the comparator to use for sorting matches in
     *                        getSummary()
     * @return a new ScoreboardService instance with the specified sorting
     * @throws IllegalArgumentException if matchComparator is null
     */
    public static ScoreboardService create(Comparator<Match> matchComparator) {
        if (matchComparator == null) {
            throw new IllegalArgumentException("Match comparator cannot be null");
        }
        MatchRepository repository = new InMemoryMatchRepository();
        return new ScoreboardServiceImpl(repository, matchComparator);
    }
}