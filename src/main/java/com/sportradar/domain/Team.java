package com.sportradar.domain;

import java.util.Objects;

/**
 * Represents a football team.
 * <p>
 * A team is identified by its name. Team objects are immutable - once created,
 * they cannot be changed.
 * Two teams are considered equal if they have the same name.
 * </p>
 *
 */
public final class Team {
    private final String name;

    /**
     * Creates a new team with the specified name.
     *
     * @param name the name of the team
     * @throws IllegalArgumentException if the name is null or empty
     */
    public Team(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Team name cannot be null or empty");
        }
        this.name = name;
    }

    /**
     * Returns the name of this team.
     *
     * @return the team name
     */
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Two teams are considered equal if they have the same name.
     * </p>
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Team team = (Team) o;
        return Objects.equals(name, team.name);
    }

    /**
     * {@inheritDoc}
     * <p>
     * The hash code is computed using the team name.
     * </p>
     */
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Returns the name of this team as its string representation.
     * </p>
     */
    @Override
    public String toString() {
        return name;
    }
}