package com.zarterstein.magid.api.reveries;

import java.util.Objects;

/**
 * A float reverie, type name {@value #TYPE}. Immutable; equal to another FloatReverie with an equal value.
 * There is no double reverie on purpose.
 *
 * @param value the value; never {@code null}
 * @author Mark-Marion Gondelman (Orchestration)
 * @author Code: Claude Code (agentic implementation)
 */
public record FloatReverie(Float value) implements Reverie<Float> {

    /** The type name. Saves store it: never change it, or old saves no longer restore. */
    public static final String TYPE = FLOAT;

    /**
     * Creates the reverie.
     *
     * @param value the value
     * @throws NullPointerException if {@code value} is {@code null}
     */
    public FloatReverie {
        Objects.requireNonNull(value, "value");
    }

    @Override
    public Float get() {
        return value;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    /** @return the value as text, e.g. {@code "0.5"}: what narration and messages show */
    @Override
    public String toString() {
        return value.toString();
    }
}
