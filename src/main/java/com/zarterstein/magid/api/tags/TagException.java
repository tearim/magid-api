package com.zarterstein.magid.api.tags;

/**
 * A tag can't run: the author wrote it wrong, or it was used where it can't work. The engine stops the story's action
 * and reports the message to the author, so the message should say what to change in the story.
 * <p>
 * Give the {@link SourcePosition} of the element (or attribute) whenever you have it: the message then ends with it.
 * Use the subclasses where they fit, {@link TagAttributeException} for a missing or bad attribute and
 * {@link TagContextException} for a tag in the wrong place. The engine's own tag exceptions extend these classes.
 * </p>
 * <p>
 * Unchecked, like the engine's exceptions: a tag may declare it, but nobody has to catch it.
 * </p>
 *
 * <pre>{@code
 * throw new TagException("<roll> can't roll a negative number of dice", position);
 * }</pre>
 *
 * @author Mark-Marion Gondelman (Orchestration)
 * @author Code: Claude Code (agentic implementation)
 */
public class TagException extends RuntimeException {

    private final SourcePosition position;

    /**
     * @param message what is wrong, for the author
     */
    public TagException(String message) {
        this(message, null, null);
    }

    /**
     * @param message  what is wrong, for the author
     * @param position where; {@link SourcePosition#UNKNOWN} (or {@code null}) if unknown
     */
    public TagException(String message, SourcePosition position) {
        this(message, position, null);
    }

    /**
     * @param message  what is wrong, for the author
     * @param position where; {@link SourcePosition#UNKNOWN} (or {@code null}) if unknown
     * @param cause    the exception that made the tag fail; {@code null} if none
     */
    public TagException(String message, SourcePosition position, Throwable cause) {
        super(position == null || !position.isKnown() ? message : message + " (" + position.describe() + ")", cause);
        this.position = position == null ? SourcePosition.UNKNOWN : position;
    }

    /**
     * @return where the error is; {@link SourcePosition#UNKNOWN} if unknown. {@link #getMessage()} already ends with a
     *         known position
     */
    public SourcePosition position() {
        return position;
    }
}
