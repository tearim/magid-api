package com.zarterstein.magid.api.tags;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The default methods of {@link StoryObjectStash} ({@code computeIfAbsent}, {@code close}), over a minimal map-backed
 * stash.
 *
 * @author Mark-Marion Gondelman (Orchestration)
 * @author Code: Claude Code (agentic implementation)
 */
class StoryObjectStashTest {

    private static final String KEY = "magid-test/history";

    /** A stash over a map; {@code clear} counts its calls. */
    static final class MapStash implements StoryObjectStash {
        final Map<String, Object> objects = new HashMap<>();
        int clears = 0;

        public Object get(String key) { return objects.get(key); }
        public boolean has(String key) { return objects.containsKey(key); }
        public void put(String key, Object value) { if (value == null) objects.remove(key); else objects.put(key, value); }
        public void remove(String key) { objects.remove(key); }
        public Set<String> keys() { return Set.copyOf(objects.keySet()); }
        public void clear() { clears++; objects.clear(); }
    }

    private final MapStash stash = new MapStash();

    @Test
    void computeIfAbsentCreatesAndStores() {
        Object created = stash.computeIfAbsent(KEY, ArrayList::new);

        assertSame(created, stash.get(KEY));
    }

    @Test
    void computeIfAbsentReusesTheStoredObject() {
        List<String> stored = new ArrayList<>();
        stash.put(KEY, stored);

        assertSame(stored, stash.computeIfAbsent(KEY, ArrayList::new));
    }

    @Test
    void computeIfAbsentDoesNotCallTheFactoryWhenStored() {
        stash.put(KEY, "there");

        stash.computeIfAbsent(KEY, () -> fail("the factory was called"));
    }

    @Test
    void computeIfAbsentWithANullFactoryResultStoresNothing() {
        assertNull(stash.computeIfAbsent(KEY, () -> null));
        assertFalse(stash.keys().contains(KEY));
    }

    @Test
    void closeClears() {
        stash.put(KEY, "x");

        stash.close();

        assertEquals(1, stash.clears);
        assertTrue(stash.keys().isEmpty());
    }
}
