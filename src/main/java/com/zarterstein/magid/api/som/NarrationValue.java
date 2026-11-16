package com.zarterstein.magid.api.som;

import com.zarterstein.magid.api.values.BasicTypedValue;

import java.util.Objects;

/**
 * The value of one field of a narrated line ({@link StoryObjectModel#narrate(java.util.Map)}): a text, an integer, a
 * float or a boolean, which the engine sends to the client as the matching JSON value.
 * <pre>{@code
 * som.narrate(Map.of(
 *         "text",       NarrationValue.of("I am pleased with your work"),
 *         "text-class", NarrationValue.of("duke"),
 *         "width",      NarrationValue.of(320)));
 * }</pre>
 * Prefer the {@code of(…)} factories: they accept only the four types. The constructor checks at run time.
 *
 * @param value the value: a {@code String}, {@code Integer}, {@code Float} or {@code Boolean}; never {@code null}
 * @param <T>   the Java type of the value
 * @author Mark-Marion Gondelman (Orchestration)
 * @author Code: Claude Code (agentic implementation)
 */
public record NarrationValue<T>(T value) implements BasicTypedValue<T> {

    /**
     * Creates the value.
     *
     * @param value the value
     * @throws NullPointerException     if {@code value} is {@code null}
     * @throws IllegalArgumentException if {@code value} isn't a {@code String}, {@code Integer}, {@code Float} or
     *                                  {@code Boolean}
     */
    public NarrationValue {
        Objects.requireNonNull(value, "value");
        if ( !(value instanceof String || value instanceof Integer || value instanceof Float
                || value instanceof Boolean) ) {
            throw new IllegalArgumentException("A narration value is a String, Integer, Float or Boolean, not "
                    + value.getClass().getName());
        }
    }

    /**
     * @param value a text
     * @return a narration value holding it
     */
    public static NarrationValue<String> of(String value) {
        return new NarrationValue<>(value);
    }

    /**
     * @param value an integer
     * @return a narration value holding it
     */
    public static NarrationValue<Integer> of(int value) {
        return new NarrationValue<>(value);
    }

    /**
     * @param value a float
     * @return a narration value holding it
     */
    public static NarrationValue<Float> of(float value) {
        return new NarrationValue<>(value);
    }

    /**
     * @param value a boolean
     * @return a narration value holding it
     */
    public static NarrationValue<Boolean> of(boolean value) {
        return new NarrationValue<>(value);
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public String getType() {
        return switch (value) {
            case String _ -> STRING;
            case Integer _ -> INTEGER;
            case Float _ -> FLOAT;
            default -> BOOLEAN;     // the constructor admits nothing else
        };
    }

    /** @return the value as text, e.g. {@code "320"} */
    @Override
    public String toString() {
        return value.toString();
    }
}
