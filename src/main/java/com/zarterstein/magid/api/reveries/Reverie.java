package com.zarterstein.magid.api.reveries;

/**
 * A typed value held by a story: a variable or constant, an actor's memory, an auxiliary attribute.
 * <p>
 * Reveries are immutable. MagidML is dynamically typed: a symbol can hold an integer now and a text later. That
 * changes which reverie the symbol holds (the engine replaces it), never the reverie itself; to change a symbol, go
 * through the engine's symbols, which check types and constants.
 * </p>
 * <p>
 * The set of types is open: {@link #getType()} names it, and new types may be added in later versions, so a
 * {@code switch} over reveries needs a {@code default} branch. The built-in types are {@link BooleanReverie},
 * {@link IntegerReverie}, {@link FloatReverie} and {@link StringReverie}. There is no double on purpose: Magid is a
 * story engine, not a calculator.
 * </p>
 *
 * @param <T> the Java type of the value
 * @author Mark-Marion Gondelman (Orchestration)
 * @author Code: Claude Code (agentic implementation)
 */
public interface Reverie<T> {

    /**
     * The value.
     *
     * @return the value; never {@code null}
     */
    T get();

    /**
     * The type's name.
     *
     * @return the type name, e.g. {@value IntegerReverie#TYPE}. Saves store it, so the names of the built-in types
     * never change.
     */
    String getType();

    /**
     * Wraps a boolean.
     *
     * @param value a boolean
     * @return a {@link BooleanReverie} holding it
     */
    static Reverie<Boolean> of(boolean value) {
        return new BooleanReverie(value);
    }

    /**
     * Wraps an integer.
     *
     * @param value an integer
     * @return an {@link IntegerReverie} holding it
     */
    static Reverie<Integer> of(int value) {
        return new IntegerReverie(value);
    }

    /**
     * Wraps a float.
     *
     * @param value a float
     * @return a {@link FloatReverie} holding it
     */
    static Reverie<Float> of(float value) {
        return new FloatReverie(value);
    }

    /**
     * Wraps a text as it is: {@code "42"} stays the text {@code "42"}. Converting text to its closest type is the
     * engine's rule, not part of this contract.
     *
     * @param value a text; not {@code null}
     * @return a {@link StringReverie} holding it
     */
    static Reverie<String> of(String value) {
        return new StringReverie(value);
    }
}
