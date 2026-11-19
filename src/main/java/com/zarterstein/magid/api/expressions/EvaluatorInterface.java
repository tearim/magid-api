package com.zarterstein.magid.api.expressions;

/**
 * The story's expressions: {@code {…}} substitution in text and scripts, evaluated against the story's symbols with
 * the engine's policy (the master switch {@code magid.expressions}, permissions, time limits). When expressions are
 * disabled, nothing is evaluated: text comes back unchanged.
 *
 * <pre>{@code
 * String line = evaluator().evaluate("You have {player.gold} gold.");
 * }</pre>
 *
 * @author Mark-Marion Gondelman (Orchestration)
 * @author Code: Claude Code (agentic implementation)
 */
public interface EvaluatorInterface {

    /**
     * @return whether expressions are enabled for this engine
     */
    boolean isEnabled();

    /**
     * @param text a text
     * @return whether it contains expressions or symbol references ({@code {…}})
     */
    boolean hasExpressions(String text);

    /**
     * Substitutes the {@code {…}} parts of a text.
     *
     * @param text a text
     * @return the text with its expressions evaluated; the text unchanged if expressions are disabled; {@code ""} if
     *         the text is a single symbol ({@code {name}}) that doesn't exist; never {@code null}
     */
    String evaluate(String text);

    /**
     * Runs a script.
     *
     * @param script a script, without braces
     * @return its value; {@code null} if expressions are disabled
     */
    Object run(String script);
}
