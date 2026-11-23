package com.zarterstein.magid.api.tags;

import java.util.Set;
import java.util.function.Supplier;

/**
 * Objects a tag keeps for later in one story session: a persona's chat history, a pending request, a conversation id.
 * Plain Java objects, unlike the symbols, which hold values the story can read and the server saves.
 * <p>
 * <b>One stash per session.</b> Each story session (each player's run of a story) has its own; Alice's and Bob's
 * never meet. Something meant for every session, such as an HTTP client, belongs in a static field of the pack:
 * the pack's class loader is shared by all sessions.
 * </p>
 * <p>
 * <b>Transient.</b> Nothing in it is saved: after a restore it is empty, and the engine {@linkplain #clear() clears}
 * it when the story's XML is reloaded and when tagpacks are reloaded. What must survive a save belongs in the symbols,
 * as plain values.
 * </p>
 * <p>
 * <b>Shared by the packs.</b> Every pack's tags see the same stash, so a key starts with the pack's id:
 * {@code magid-ollama-pack/history/wizard}. Another pack can read an object only if its class comes from the JDK or
 * from magid-api: each pack has its own class loader, so a class of pack A is a different class for pack B, and a
 * cast fails with a {@link ClassCastException}.
 * </p>
 * <p>
 * <b>Not thread-safe</b>, like the rest of a session: the engine handles one request of a session at a time.
 * </p>
 *
 * @author Mark-Marion Gondelman (Creator)
 * @author JavaDocs: Claude Code (Model: claude-opus-5-5)
 */
public interface StoryObjectStash extends ObjectStash, AutoCloseable {

    /**
     * @param key the key, e.g. {@code magid-ollama-pack/history/wizard}
     * @return the object stored under it; {@code null} if there is none
     */
    Object get(String key);

    /**
     * @param key the key
     * @return whether an object is stored under it (a stash holds no {@code null}s, so this is
     *         {@code get(key) != null})
     */
    boolean has(String key);

    /**
     * Stores an object, replacing the one under the key. The replaced object is not closed: whoever replaces it
     * closes it, if it needs closing.
     *
     * @param key   the key, starting with the pack's id
     * @param value the object; {@code null} {@linkplain #remove removes} the key
     */
    void put(String key, Object value);

    /**
     * The object under a key, created and stored first if there is none: for something a tag builds once and then
     * reuses, e.g. a persona's history:
     * <pre>{@code
     * List<ChatMessage> history = (List<ChatMessage>) stash.computeIfAbsent(
     *         "magid-ollama-pack/history/" + persona, ArrayList::new);
     * }</pre>
     *
     * @param key     the key, starting with the pack's id
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
     * Removes a key. The object is not closed: the tag that removes it closes it, if it needs closing.
     *
     * @param key the key; nothing happens if it has no object
     */
    void remove(String key);

    /**
     * @return the keys that have an object, as a copy: changing the stash doesn't change it
     */
    Set<String> keys();

    /**
     * Empties the stash, every pack's objects included: each object that is {@link AutoCloseable} is closed once
     * (also when it is stored under several keys), and one failing to close doesn't stop the others. The stash stays
     * usable. The engine calls it when the story's XML or the tagpacks are reloaded; a tag hardly ever should.
     */
    void clear();

    /** {@inheritDoc} The same as {@link #clear()}: the stash stays usable. */
    @Override
    default void close() {
        clear();
    }
}
