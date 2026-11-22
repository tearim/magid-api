package com.zarterstein.magid.api.access;

import java.util.Locale;

/**
 * The rank of an account on a Magid server, from lowest to highest. Each level includes the ones below it, so a
 * moderator may do everything a user may.
 * <p>
 * A level is global (a property of the account). Relations to things, such as being the author of a story or the
 * provider of a group, are not levels: the server's access policy expresses them.
 * </p>
 * <p>
 * The list may grow. Compare levels with {@link #atLeast(AccessLevel)} rather than an exhaustive {@code switch}, so a
 * new level doesn't break a tagpack.
 * </p>
 *
 * @author Mark-Marion Gondelman (Design, Orchestration)
 * @author Code: Claude Code (agentic implementation)
 */
public enum AccessLevel {
    /** Not registered: an anonymous reader. */
    ANON,
    /** A registered user. */
    USER,
    /** Server staff. */
    MODERATOR,
    /** The person who maintains the server. */
    ADMIN;

    /**
     * @param other the level to compare with
     * @return whether this level is {@code other} or higher
     */
    public boolean atLeast(AccessLevel other) {
        return compareTo(other) >= 0;
    }

    /**
     * Parses a level name, ignoring case ({@code "user"}, {@code "ADMIN"}).
     *
     * @param name the level name
     * @return the level
     * @throws IllegalArgumentException when the name is no level
     */
    public static AccessLevel parse(String name) {
        try {
            return valueOf(name.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("Unknown access level: '" + name + "'");
        }
    }
}
