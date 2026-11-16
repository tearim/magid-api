package com.zarterstein.magid.api.tags;

import java.util.Objects;

/**
 * What a tag returns: the text it produced, which the engine places where the tag stood, and the {@link Outcome},
 * what the story does next. Styled output (a line with a text class) goes through the SOM's {@code narrate} instead.
 * <p>
 * For {@link Outcome.Type#BREAK} and the two kinds of stay, the text is also the message they carry, as with
 * {@code <stay message="…">}.
 * </p>
 * <pre>{@code
 * return TagResult.text("The Duke nods.");          // text, then go on
 * return TagResult.of(Outcome.jump("castle"));      // no text, go to the castle
 * return new TagResult("You fall.", Outcome.die("pit"));
 * }</pre>
 *
 * @param text    the produced text; {@code ""} for none ({@code null} becomes {@code ""})
 * @param outcome what happens next; never {@code null}
 * @author Mark-Marion Gondelman (Creator)
 * @author JavaDocs: Claude Code (Model: claude-opus-5-5)
 */
public record TagResult(String text, Outcome outcome) {

    /**
     * Creates a result.
     *
     * @throws NullPointerException if {@code outcome} is {@code null}
     */
    public TagResult {
        Objects.requireNonNull(outcome, "outcome");
        if ( text == null ) text = "";
    }

    /**
     * @param text the produced text
     * @return the text, then go on ({@link Outcome#proceed()})
     */
    public static TagResult text(String text) { return new TagResult(text, Outcome.proceed()); }

    /**
     * @param outcome what happens next
     * @return no text, and that outcome
     */
    public static TagResult of(Outcome outcome) { return new TagResult("", outcome); }
}
