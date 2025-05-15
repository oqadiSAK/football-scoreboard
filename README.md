# Live Football World Cup Scoreboard
A simple Java library implementation for tracking live football matches and their scores during the World Cup.

## Requirements
The library implements the following operations:
1. **Starting a match**: Create a new match with initial score 0-0, specifying:
   - Home team
   - Away team
2. **Updating scores**: Update the absolute score for both teams in a match
3. **Finishing a match**: Remove a match from the scoreboard
4. **Getting a summary**: Retrieve all ongoing matches ordered by:
   - Total score (descending)
   - Most recently started (for matches with the same total score)

## Implementation Details
### Technology Stack
- Java 17
- Maven for dependency management
- JUnit 5 and Mockito for testing

### Architecture
The implementation follows a layered architecture with clear separation of concerns:
- **Domain Layer**: Contains the core business entities (`Match`, `Team`, `Score`)
- **Repository Layer**: Handles data storage operations 
- **Service Layer**: Implements the business logic
- **Factory**: Provides easy instantiation of the scoreboard service

### Design Patterns & Principles
- **Factory Pattern**: Used to create preconfigured instances of the `ScoreboardService`
- **Repository Pattern**: Abstracts data access operations
- **SOLID Principles**:
  - Single Responsibility: Each class has a single purpose
  - Open/Closed: Extended through composition (e.g., customizable comparator)
  - Liskov Substitution: Proper use of interfaces
  - Interface Segregation: Focused interfaces
  - Dependency Inversion: Dependencies on abstractions, not concrete implementations
- **Immutability**: Core entities like `Team` and `Score` are immutable
- **Defensive Programming**: Parameter validation and clear exception handling

### Documentation
Since this is a library implementation, javadoc comments are added in the public facing components and you can find the generated docs in `docs/index.html`

### Assumptions
1. **Team Uniqueness**: Teams are uniquely identified by their name.
2. **Team Availability**: A team can only play in one match at a time. Attempting to start a match with a team that's already playing will throw a `TeamAlreadyPlayingException`.
3. **Match Identification**: Matches are identified by the combination of home and away teams. The order matters (home vs. away).
4. **Score Updates**: Score updates are absolute (not incremental). Each update represents the current total score for both teams.
5. **Score Validation**: Scores cannot be negative numbers.
6. **No Ties Between Matches**: When two matches have the same total score, they are ordered by start time (most recent first).
7. **In-Memory Storage**: As per requirements, the implementation uses an in-memory store with no persistence.
8. **Thread Safety**: The current implementation is not thread-safe. Synchronization would need to be added for concurrent usage.

## Building the Project

To build the project and run tests:

```bash
mvn clean install
```

## Usage Examples
### Basic Usage
```java
// Create the scoreboard service
ScoreboardService scoreboard = ScoreboardServiceFactory.createDefault();

// Start a new match
scoreboard.startMatch("Mexico", "Canada");

// Update the score
scoreboard.updateScore("Mexico", "Canada", 0, 5);

// Start more matches and update their scores
scoreboard.startMatch("Spain", "Brazil");
scoreboard.updateScore("Spain", "Brazil", 10, 2);

// Get a summary of all ongoing matches
List<Match> summary = scoreboard.getSummary();
summary.forEach(System.out::println);

// Finish a match
scoreboard.finishMatch("Mexico", "Canada");
```

### Custom Sorting
```java
// Create a scoreboard with custom sorting (alphabetical by home team name)
Comparator<Match> alphabeticalByHomeTeam = Comparator.comparing(m -> m.getHomeTeam().getName());
ScoreboardService customScoreboard = ScoreboardServiceFactory.create(alphabeticalByHomeTeam);
```

### Complete Example
A complete, runnable example demonstrating all features of the library can be found in the `src/main/java/com/sportradar/example/ScoreboardExample.java` file.

You can run the example using Maven:

```bash
mvn exec:java -Dexec.mainClass="com.sportradar.example.ScoreboardExample"
```