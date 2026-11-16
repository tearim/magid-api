package com.zarterstein.magid.api.reveries;

import com.zarterstein.magid.api.values.BasicTypedValue;

/**
 * A typed value held by a story: a variable or constant, an actor's memory, an auxiliary attribute.
 * <p>
 * Reveries are immutable. MagidML is dynamically typed: a symbol can hold an integer now and a text later. That
 * changes which reverie the symbol holds (the engine replaces it), never the reverie itself; to change a symbol, go
 * through the engine's symbols, which check types and constants.
 * </p>
 * <p>
 * The value and its type name come from {@link BasicTypedValue}, which also holds the rules on types (an open set, no
 * double). The built-in reveries are {@link BooleanReverie}, {@link IntegerReverie}, {@link FloatReverie} and
 * {@link StringReverie}; saves store their type names, so those never change.
 * </p>
 *
 * @param <T> the Java type of the value
 * @author Mark-Marion Gondelman (Orchestration)
 * @author Code: Claude Code (agentic implementation)
 */
public interface Reverie<T> extends BasicTypedValue<T> {

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
