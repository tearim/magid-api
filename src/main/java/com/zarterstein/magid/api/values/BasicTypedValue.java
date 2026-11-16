package com.zarterstein.magid.api.values;

/**
 * A single value of one of Magid's basic types, with the type's name: what the engine stores, shows and sends to
 * clients. Each use has its own kind: {@link com.zarterstein.magid.api.reveries.Reverie} for what a story holds
 * (variables, constants, actors' memories), {@link com.zarterstein.magid.api.som.NarrationValue} for a field of a
 * narrated line. Where any typed value will do (narration, for instance, also accepts a recalled reverie), the API
 * takes this interface.
 * <p>
 * The built-in types map one to one onto JSON's scalars: {@value #STRING} (a JSON string), {@value #INTEGER} and
 * {@value #FLOAT} (JSON numbers), {@value #BOOLEAN} (a JSON boolean). The set is open, so a {@code switch} over
 * {@link #getType()} needs a {@code default} branch. There is no double on purpose: Magid is a story engine, not a
 * calculator.
 * </p>
 *
 * @param <T> the Java type of the value
 * @author Mark-Marion Gondelman (Orchestration)
 * @author Code: Claude Code (agentic implementation)
 */
public interface BasicTypedValue<T> {

    /** Type name of a text ({@code String}). */
    String STRING = "string";
    /** Type name of an integer ({@code Integer}). */
    String INTEGER = "integer";
    /** Type name of a float ({@code Float}). */
    String FLOAT = "float";
    /** Type name of a boolean ({@code Boolean}). */
    String BOOLEAN = "boolean";

    /**
     * The value.
     *
     * @return the value; never {@code null}
     */
    T get();

    /**
     * The type's name.
     *
     * @return the type name, e.g. {@value #INTEGER}. Saves store the names of reveries' types, so the names of the
     * built-in types never change.
     */
    String getType();
}
