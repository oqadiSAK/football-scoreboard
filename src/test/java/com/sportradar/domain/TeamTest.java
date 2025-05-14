package com.sportradar.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Team")
class TeamTest {

    @Nested
    @DisplayName("creation")
    class Creation {
        @Test
        @DisplayName("succeeds with valid name")
        void shouldCreateWithValidName() {
            // Given
            var teamName = "Brazil";

            // When
            var team = new Team(teamName);

            // Then
            assertEquals(teamName, team.getName());
        }

        @ParameterizedTest(name = "fails with invalid name: {0}")
        @NullAndEmptySource
        @ValueSource(strings = { " ", "  \t" })
        @DisplayName("fails with invalid name")
        void shouldThrowExceptionForInvalidName(String invalidName) {
            // When/Then
            var exception = assertThrows(IllegalArgumentException.class,
                    () -> new Team(invalidName));
            assertTrue(exception.getMessage().contains("name cannot be null or empty"));
        }
    }

    @Nested
    @DisplayName("equality")
    class Equality {
        @Test
        @DisplayName("considers teams with same name equal")
        void shouldConsiderTeamsWithSameNameEqual() {
            // Given
            var team1 = new Team("Germany");
            var team2 = new Team("Germany");

            // Then
            assertEquals(team1, team2);
            assertEquals(team1.hashCode(), team2.hashCode());
        }

        @Test
        @DisplayName("considers teams with different names not equal")
        void shouldConsiderTeamsWithDifferentNamesNotEqual() {
            // Given
            var team1 = new Team("Germany");
            var team2 = new Team("France");

            // Then
            assertNotEquals(team1, team2);
        }
    }

    @Test
    @DisplayName("returns name in toString()")
    void shouldReturnNameInToString() {
        // Given
        var team = new Team("Spain");

        // Then
        assertEquals("Spain", team.toString());
    }
}