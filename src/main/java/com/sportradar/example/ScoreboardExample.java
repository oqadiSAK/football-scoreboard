package com.sportradar.example;

import java.util.List;
import java.util.Comparator;

import com.sportradar.domain.Match;
import com.sportradar.exception.MatchNotFoundException;
import com.sportradar.exception.TeamAlreadyPlayingException;
import com.sportradar.factory.ScoreboardServiceFactory;
import com.sportradar.service.ScoreboardService;

public class ScoreboardExample {

    public static void main(String[] args) {
        basicUsageExample();
        System.out.println("\n--------------------------------------------\n");
        advancedExample();
    }

    private static void basicUsageExample() {
        System.out.println("BASIC USAGE EXAMPLE");
        System.out.println("==================\n");

        ScoreboardService scoreboard = ScoreboardServiceFactory.createDefault();

        System.out.println("Starting matches...");
        scoreboard.startMatch("Mexico", "Canada");
        scoreboard.startMatch("Spain", "Brazil");
        scoreboard.startMatch("Germany", "France");
        scoreboard.startMatch("Uruguay", "Italy");
        scoreboard.startMatch("Argentina", "Australia");

        System.out.println("Updating scores...");
        scoreboard.updateScore("Mexico", "Canada", 0, 5);
        scoreboard.updateScore("Spain", "Brazil", 10, 2);
        scoreboard.updateScore("Germany", "France", 2, 2);
        scoreboard.updateScore("Uruguay", "Italy", 6, 6);
        scoreboard.updateScore("Argentina", "Australia", 3, 1);

        System.out.println("\nSummary of matches (ordered by total score, then by most recent):");
        List<Match> summary = scoreboard.getSummary();
        printSummary(summary);

        System.out.println("\nFinishing the Mexico vs Canada match...");
        scoreboard.finishMatch("Mexico", "Canada");

        System.out.println("\nUpdated summary after finishing a match:");
        summary = scoreboard.getSummary();
        printSummary(summary);
    }

    private static void advancedExample() {
        System.out.println("ADVANCED EXAMPLE");
        System.out.println("===============\n");

        Comparator<Match> homeTeamAlphabetical = Comparator.comparing(match -> match.getHomeTeam().getName());

        ScoreboardService scoreboard = ScoreboardServiceFactory.create(homeTeamAlphabetical);

        System.out.println("Starting matches...");
        scoreboard.startMatch("Brazil", "Croatia");
        scoreboard.startMatch("Argentina", "Nigeria");
        scoreboard.startMatch("France", "Australia");

        scoreboard.updateScore("Brazil", "Croatia", 3, 1);
        scoreboard.updateScore("Argentina", "Nigeria", 2, 1);
        scoreboard.updateScore("France", "Australia", 4, 1);

        System.out.println("\nSummary sorted alphabetically by home team:");
        List<Match> summary = scoreboard.getSummary();
        printSummary(summary);

        System.out.println("\nDemonstrating exception handling:");

        try {
            System.out.println("Attempting to start a match with a team already playing (Brazil vs Germany)...");
            scoreboard.startMatch("Brazil", "Germany");
        } catch (TeamAlreadyPlayingException e) {
            System.out.println("Caught exception: " + e.getMessage());
        }

        try {
            System.out.println("Attempting to update a non-existent match (Spain vs Portugal)...");
            scoreboard.updateScore("Spain", "Portugal", 2, 2);
        } catch (MatchNotFoundException e) {
            System.out.println("Caught exception: " + e.getMessage());
        }

        try {
            System.out.println("Attempting to finish a non-existent match (England vs Italy)...");
            scoreboard.finishMatch("England", "Italy");
        } catch (MatchNotFoundException e) {
            System.out.println("Caught exception: " + e.getMessage());
        }

        System.out.println("\nCreating scoreboard with complex custom sorting...");
        Comparator<Match> awayTeamThenHomeScore = Comparator
                .comparing((Match match) -> match.getAwayTeam().getName())
                .thenComparing(Match::getHomeScore, Comparator.reverseOrder());

        ScoreboardService anotherScoreboard = ScoreboardServiceFactory.create(awayTeamThenHomeScore);

        anotherScoreboard.startMatch("Spain", "Portugal");
        anotherScoreboard.startMatch("Italy", "France");
        anotherScoreboard.startMatch("Germany", "England");

        anotherScoreboard.updateScore("Spain", "Portugal", 3, 2);
        anotherScoreboard.updateScore("Italy", "France", 1, 1);
        anotherScoreboard.updateScore("Germany", "England", 2, 0);

        System.out.println("\nSummary sorted by away team, then by home score (descending):");
        List<Match> anotherSummary = anotherScoreboard.getSummary();
        printSummary(anotherSummary);
    }

    private static void printSummary(List<Match> matches) {
        if (matches.isEmpty()) {
            System.out.println("No matches in progress.");
            return;
        }

        for (int i = 0; i < matches.size(); i++) {
            Match match = matches.get(i);
            System.out.printf("%d. %s %d - %d %s%n",
                    i + 1,
                    match.getHomeTeam().getName(),
                    match.getHomeScore(),
                    match.getAwayScore(),
                    match.getAwayTeam().getName());
        }
    }
}