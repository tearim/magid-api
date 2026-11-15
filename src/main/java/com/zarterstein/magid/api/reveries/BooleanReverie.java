package com.zarterstein.magid.api.reveries;

import java.util.Objects;

/**
 * A boolean reverie, type name {@value #TYPE}. Immutable; equal to another BooleanReverie with an equal value.
 *
 * @param value the value; never {@code null}
 * @author Mark-Marion Gondelman (Orchestration)
 * @author Code: Claude Code (agentic implementation)
 */
public record BooleanReverie(Boolean value) implements Reverie<Boolean> {

    /** The type name. Saves store it: never change it, or old saves no longer restore. */
    public static final String TYPE = "boolean";

    /**
     * @param value the value
     * @throws NullPointerException if {@code value} is {@code null}
     */
    public BooleanReverie {
        Objects.requireNonNull(value, "value");
    }

    @Override
    public Boolean get() {
        return value;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    /** @return the value as text, e.g. {@code "true"}: what narration and messages show */
    @Override
    public String toString() {
        return value.toString();
    }
}
