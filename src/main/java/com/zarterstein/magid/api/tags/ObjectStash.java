package com.zarterstein.magid.api.tags;

import java.util.Set;
import java.util.function.Supplier;

/**
 * Plain Java objects a tag keeps for later, under string keys: what the session's {@link StoryObjectStash} and the
 * caller's {@link com.zarterstein.magid.api.access.CallerStash} have in common. Nothing in a stash is ever saved.
 * <p>
 * <b>Classes.</b> Each pack has its own class loader, so a class of pack A is a different class for pack B. Objects
 * of JDK or magid-api classes can be read by every pack; {@link #getIfClass} returns {@code null} instead of failing
 * when the class doesn't match.
 * </p>
 *
 * @author Mark-Marion Gondelman (Design, Orchestration)
 * @author Code: Claude Code (agentic implementation)
 */
public interface ObjectStash {

    /**
     * @param key the key
     * @return the object stored under it; {@code null} if there is none
     */
    Object get(String key);

    /**
     * The object under a key, if it is of the given type: for reading what another tag, pack or story may have put
     * there, without a cast that can fail.
     *
     * @param key  the key
     * @param type the type expected
     * @param <T>  the type expected
     * @return the object, if there is one and it is an instance of {@code type} (of that very class, from that very
     *         class loader); {@code null} otherwise
     */
    default <T> T getIfClass(String key, Class<T> type) {
        Object value = get(key);
        return type.isInstance(value) ? type.cast(value) : null;
    }

    /**
     * @param key the key
     * @return whether an object is stored under it (a stash holds no {@code null}s)
     */
    boolean has(String key);

    /**
     * Stores an object, replacing the one under the key. The replaced object is not closed.
     *
     * @param key   the key
     * @param value the object; {@code null} {@linkplain #remove removes} the key
     */
    void put(String key, Object value);

    /**
     * The object under a key, created and stored first if there is none.
     *
     * @param key     the key
     * @param factory creates the object; called only if the key has none
     * @return the stored or new object; {@code null} if the factory gives {@code null}, which stores nothing
     */
    default Object computeIfAbsent(String key, Supplier<?> factory) {
        Object value = get(key);
        if (value == null) {
            value = factory.get();
            if (value != null) put(key, value);
        }
        return value;
    }

    /**
     * Removes a key. The object is not closed.
     *
     * @param key the key; nothing happens if it has no object
     */
    void remove(String key);

    /**
     * @return the keys that have an object, as a copy
     */
    Set<String> keys();

    /** Empties the stash (what "the stash" covers is up to the subtype). */
    void clear();
}
