package com.zarterstein.magid.api.reveries;

import java.util.Objects;

/**
 * A text reverie, type name {@value #TYPE}. Immutable; equal to another StringReverie with an equal value.
 *
 * @param value the value; never {@code null}
 * @author Mark-Marion Gondelman (Orchestration)
 * @author Code: Claude Code (agentic implementation)
 */
public record StringReverie(String value) implements Reverie<String> {

    /** The type name. Saves store it: never change it, or old saves no longer restore. */
    public static final String TYPE = "string";

    /**
     * @param value the value
     * @throws NullPointerException if {@code value} is {@code null}
     */
    public StringReverie {
        Objects.requireNonNull(value, "value");
    }

    @Override
    public String get() {
        return value;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    /** @return the value as text, e.g. {@code "Ada"}: what narration and messages show */
    @Override
    public String toString() {
        return value.toString();
    }
}
