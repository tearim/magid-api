package com.zarterstein.magid.api.tags;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link ObjectStash#getIfClass}, over the map-backed stash of {@link StoryObjectStashTest}.
 *
 * @author Mark-Marion Gondelman (Orchestration)
 * @author Code: Claude Code (agentic implementation)
 */
class ObjectStashTest {

    private static final String KEY = "magid-test/greeting";

    private final StoryObjectStashTest.MapStash stash = new StoryObjectStashTest.MapStash();

    @Test
    void getIfClassGivesAnObjectOfTheType() {
        stash.put(KEY, "hello");

        assertEquals("hello", stash.getIfClass(KEY, String.class));
    }

    @Test
    void getIfClassGivesAnObjectOfASubtype() {
        stash.put(KEY, List.of("a", "b"));

        assertEquals(List.of("a", "b"), stash.getIfClass(KEY, List.class));
    }

    @Test
    void getIfClassGivesNullForAnotherType() {
        stash.put(KEY, 42);

        assertNull(stash.getIfClass(KEY, String.class));
    }

    @Test
    void getIfClassGivesNullForAMissingKey() {
        assertNull(stash.getIfClass(KEY, String.class));
    }
}
