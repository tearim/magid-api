package com.zarterstein.magid.api.access;

import com.zarterstein.magid.api.tags.ObjectStash;

import java.util.Set;
import java.util.function.Supplier;

/**
 * Objects that follow a caller from session to session and from story to story: quotas, token budgets, rate
 * limits, conversation memory, one story asking about another. A tag reaches it through {@link Caller#stash()}.
 * <p>
 * <b>Shared by everything the caller runs.</b> Every tag of every pack, in every story the same user runs, sees what
 * any other tag placed in that user's stash, and may change it (DECISIONS.md D24). Keep secrets (API keys, tokens)
 * out of it, unless you control every story and pack on the server. Each user has their own stash; an anonymous
 * player's lasts one session.
 * </p>
 * <p>
 * <b>Namespaces.</b> {@link Caller#stash()} is bound by the engine to the asking tag's story and pack, so plain keys of
 * two stories or two packs never collide. {@link #at(String)} and {@link #at(String, String)} give another
 * namespace's stash, to read and to write.
 * </p>
 * <p>
 * <b>A cache, not storage.</b> Never saved. It lives while the user has a session on the server, and may be empty at
 * any time before: after {@code reload-packs}, after a restart. Quotas built on it are best-effort.
 * </p>
 * <p>
 * <b>Thread-safe.</b> A user's sessions run in parallel (two tabs, two stories), so every method is safe to call
 * concurrently, and {@link #computeIfAbsent} is atomic.
 * </p>
 * <p>
 * <b>What to keep.</b> Objects of JDK or magid-api classes: an object of a pack's own class outlives the sessions and
 * would keep an old class loader alive. A stored object is a shared reference, so whoever reads a mutable list can
 * change it: prefer immutable values ({@code String}, {@code List.copyOf}, records of JDK types).
 * </p>
 *
 * @author Mark-Marion Gondelman (Design, Orchestration)
 * @author Code: Claude Code (agentic implementation)
 */
public interface CallerStash extends ObjectStash {

    /**
     * The stash of contexts without a caller ({@link Caller#NONE}): it stores nothing. {@code get} gives
     * {@code null}, {@code computeIfAbsent} gives the factory's object without keeping it, and {@code at} gives this
     * stash again. {@link Caller#NONE} is one object shared by every such context, so its stash must hold nothing.
     */
    CallerStash NONE = new CallerStash() {
        @Override
        public String storyId() {
            return "";
        }

        @Override
        public String packId() {
            return "";
        }

        @Override
        public CallerStash at(String packId) {
            return this;
        }

        @Override
        public CallerStash at(String storyId, String packId) {
            return this;
        }

        @Override
        public Object get(String key) {
            return null;
        }

        @Override
        public boolean has(String key) {
            return false;
        }

        @Override
        public void put(String key, Object value) {
        }

        @Override
        public Object computeIfAbsent(String key, Supplier<?> factory) {
            return factory.get();
        }

        @Override
        public void remove(String key) {
        }

        @Override
        public Set<String> keys() {
            return Set.of();
        }

        @Override
        public void clear() {
        }

        @Override
        public String toString() {
            return "CallerStash.NONE";
        }
    };

    /**
     * @return the story of this namespace: the story's id as the server lists it (on the Magid server, its main
     *         file relative to the content directory, e.g. {@code oasis/oasis.xml}); empty for {@link #NONE}
     */
    String storyId();

    /**
     * @return the pack of this namespace ({@code core} for the engine's own tags); empty for {@link #NONE}
     */
    String packId();

    /**
     * @param packId another pack (or this one)
     * @return the caller's stash for that pack in this story
     */
    CallerStash at(String packId);

    /**
     * @param storyId another story (or this one): its id, as {@link #storyId()}
     * @param packId  another pack (or this one)
     * @return the caller's stash for that pack in that story
     */
    CallerStash at(String storyId, String packId);

    /**
     * {@inheritDoc}
     * <p>Atomic: of two sessions asking at once, one creates the object and both get it.</p>
     */
    @Override
    Object computeIfAbsent(String key, Supplier<?> factory);

    /** Empties this namespace (this story and pack) only. */
    @Override
    void clear();
}
