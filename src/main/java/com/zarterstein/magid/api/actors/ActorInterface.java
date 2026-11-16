package com.zarterstein.magid.api.actors;

import com.zarterstein.magid.api.reveries.Reverie;

import java.util.List;
import java.util.Optional;

/**
 * An actor of the story (a character, the player, the world) as a tag sees it: its names, its memories
 * ({@code actor.key}) and its attributes ({@code actor::key}).
 * <p>
 * Writes go through the story's {@link com.zarterstein.magid.api.symbols.SymbolPinax}: {@code remember("gold", "5")}
 * on actor {@code hero} is {@code set("hero.gold", "5")}, with the same type conversion and checks as every MagidML
 * tag, and expressions see the new value at once. So this interface is a friendlier way to write symbols, not a way
 * around them.
 * </p>
 *
 * @author Mark-Marion Gondelman (Creator)
 * @author JavaDocs: Claude Code (Model: claude-opus-5-5)
 */
public interface ActorInterface {

    /** @return the actor's id, the {@code hero} in {@code hero.gold}; it never changes */
    String getId();

    /** @return the actor's name, as the story shows it */
    String getName();

    /** @param name the actor's new name; the id stays */
    void setName(String name);

    /** @return the name to show; the same as {@link #getName()} unless the engine's actor says otherwise */
    String displayName();

    /** @param name the name to show */
    void setDisplayName(String name);

    /**
     * Stores a memory, converted to the closest type ({@code "42"} becomes an integer).
     *
     * @param what  the memory's key
     * @param value its value
     */
    void remember(String what, String value);

    /**
     * Stores a memory with exactly the reverie's type: {@code Reverie.of("007")} stays a string.
     *
     * @param what  the memory's key
     * @param value its value
     */
    void remember(String what, Reverie<?> value);

    /** @param what the memory to remove */
    void forget(String what);

    /** @return the keys of the actor's memories */
    List<String> listMemories();

    /**
     * @param what a memory's key
     * @return whether the actor has that memory
     */
    boolean recollects(String what);

    /**
     * @param what a memory's key
     * @return the memory, or empty if the actor doesn't have it
     */
    Optional<Reverie<?>> recall(String what);

    /**
     * @param what a memory's key
     * @return the memory as text, or {@code ""} if the actor doesn't have it
     */
    String recallText(String what);

    /**
     * @param what  the attribute's name
     * @param value its text
     */
    void setAttribute(String what, String value);

    /**
     * @param what an attribute's name
     * @return its text, or {@code null} if the actor doesn't have it
     */
    String getAttribute(String what);

    /** @param what the attribute to remove */
    void clearAttribute(String what);

    /**
     * @param what an attribute's name
     * @return whether the actor has that attribute
     */
    boolean isA(String what);

    /**
     * @param what  an attribute's name
     * @param value a text
     * @return whether the attribute has that text
     */
    boolean isA(String what, String value);

    /** @return the names of the actor's attributes */
    List<String> listAttributes();
}
