package com.zarterstein.magid.api.symbols;

import com.zarterstein.magid.api.reveries.Reverie;

import java.util.Optional;

/**
 * The story's symbols as a tag sees them: read, set, declare and clear variables ({@code $x}), constants
 * ({@code #x}), memories ({@code actor.key}) and attributes ({@code actor::key}); {@link SymbolRef} describes the
 * syntax. The engine implements it, so every write goes through the same checks for every tag, the engine's own and
 * a tagpack's alike: types, constants, undeclared symbols.
 * <p>
 * The name: a pinax (Greek πίναξ, plural pinakes) is a tablet or board to write on, and by extension a list or
 * register. Callimachus' Pinakes was the catalogue of the Library of Alexandria: a register that tells you what
 * exists and where to find it, which is what this interface is for a story. Like the morphers, it uses a Greek root
 * that carries no programming baggage ("store", "repository" and "registry" all do).
 * </p>
 * <p>
 * Reading never throws: an unknown or malformed name reads as empty. Writing a malformed name (an empty key or
 * owner, e.g. {@code player.}) throws a {@link SymbolException}, and so does breaking a check whose level is
 * {@link ErrorReactionLevel#THROW}.
 * </p>
 *
 * @author Mark-Marion Gondelman (Creator)
 * @author JavaDocs: Claude Code (Model: claude-opus-5-5)
 */
public interface SymbolPinax {

    /**
     * Lifetime of a symbol, matching {@code <declare … scope="…">}: {@code STORY} is permanent (the default),
     * {@code ACTION} symbols are cleared after the next performed action, {@code SCENE} symbols when the scene
     * changes.
     */
    enum Scope { STORY, ACTION, SCENE }

    /**
     * Reads a symbol. Attributes, which are text, come back as a string reverie.
     *
     * @param symbol the symbol, e.g. {@code $gold}, {@code player.gold}, {@code player::title} or a bare {@code gold}
     * @return its value, or empty if it doesn't exist or the name is malformed
     */
    Optional<Reverie<?>> recall(String symbol);

    /**
     * Sets an existing symbol (or creates it, if the story allows undeclared symbols). A plain value is converted to
     * the closest type, as for every MagidML tag: {@code "42"} becomes an integer. A {@link Reverie} keeps its type:
     * {@code Reverie.of("007")} stays a string.
     *
     * @param symbol the symbol
     * @param value  the new value
     */
    void set(String symbol, Object value);

    /**
     * Declares a symbol with its first value, converted (or kept, for a reverie) as in {@link #set}.
     *
     * @param symbol the symbol; {@code #name} declares a constant
     * @param value  the initial value
     */
    void declare(String symbol, Object value);

    /**
     * Removes a symbol, constants included.
     *
     * @param symbol the symbol
     */
    void clear(String symbol);

    /**
     * @param symbol the symbol
     * @return whether it exists; {@code false} for a malformed name
     */
    boolean has(String symbol);

    /**
     * Resolves a symbol's kind against the story: unlike {@link SymbolRef#of}, a bare name that the story declared
     * as a constant gives {@link SymbolRef.Type#CONSTANT}.
     *
     * @param symbol the symbol
     * @return its reference; {@link SymbolRef.Type#ERROR} for a malformed name
     */
    SymbolRef info(String symbol);

    /**
     * Registers a symbol to be cleared automatically at the end of a scope. {@link Scope#STORY} symbols are
     * permanent, so registering one has no effect.
     *
     * @param symbol  the symbol, with its prefix (e.g. {@code $loot})
     * @param scope {@link Scope#ACTION} or {@link Scope#SCENE}
     */
    void markTemporary(String symbol, Scope scope);

    /** @return whether the story lets a symbol change its type (e.g. an integer set to a text) */
    boolean isDynamicCastingAllowed();

    /** @return whether the story lets tags use symbols it never declared */
    boolean areNonDeclaredSymbolsAllowed();

    /** @return how the story reacts to a value of another type written to a typed symbol */
    ErrorReactionLevel getDynamicCastLevel();

    /** @return how the story reacts to a symbol used without being declared */
    ErrorReactionLevel getNonDeclaredLevel();
}
