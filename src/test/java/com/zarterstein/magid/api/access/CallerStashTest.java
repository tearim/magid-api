package com.zarterstein.magid.api.access;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link CallerStash#NONE}, the stash that stores nothing, and {@link Caller#stash()}'s default.
 *
 * @author Mark-Marion Gondelman (Orchestration)
 * @author Code: Claude Code (agentic implementation)
 */
class CallerStashTest {

    private static final String KEY = "quota";

    @Test
    void theNoneCallersStashIsTheNoneStash() {
        assertSame(CallerStash.NONE, Caller.NONE.stash());
    }

    @Test
    void theNoneStashKeepsNothingPut() {
        CallerStash.NONE.put(KEY, "value");

        assertNull(CallerStash.NONE.get(KEY));
    }

    @Test
    void theNoneStashHasNoKeys() {
        CallerStash.NONE.put(KEY, "value");

        assertTrue(CallerStash.NONE.keys().isEmpty());
    }

    @Test
    void theNoneStashGivesTheFactorysObject() {
        assertEquals("made", CallerStash.NONE.computeIfAbsent(KEY, () -> "made"));
    }

    @Test
    void theNoneStashDoesNotKeepTheFactorysObject() {
        CallerStash.NONE.computeIfAbsent(KEY, () -> "made");

        assertFalse(CallerStash.NONE.has(KEY));
    }

    @Test
    void anotherNamespaceOfTheNoneStashIsTheNoneStash() {
        assertSame(CallerStash.NONE, CallerStash.NONE.at("oasis", "magid-math"));
    }
}
