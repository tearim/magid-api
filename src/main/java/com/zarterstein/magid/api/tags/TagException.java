package com.zarterstein.magid.api.tags;

/**
 * A tag can't run: the author wrote it wrong, or it was used where it can't work. The engine stops the story's action
 * and reports the message to the author, so the message should say what to change in the story.
 * <p>
 * Give the {@link TagElement} the error is about whenever you have it (a tag has its own as {@code element}): the
 * message then ends with the element's {@link SourcePosition}, and {@link #tagName()} names it. Use the subclasses
 * where they fit: {@link MissingTagAttributeException} and {@link BadTagAttributeException} for an attribute,
 * {@link MisplacedTagException} for a tag in the wrong place. The engine's own tag exceptions extend these classes.
 * </p>
 * <p>
 * Unchecked, like the engine's exceptions: a tag may declare it, but nobody has to catch it.
 * </p>
 *
 * <pre>{@code
 * throw new TagException("can't roll a negative number of dice", element);
 * // can't roll a negative number of dice (XML source (dice.xml): around line 12, column 4)
 * }</pre>
 *
 * @author Mark-Marion Gondelman (Design, Orchestration)
 * @author Code: Claude Code (agentic implementation)
 */
public class TagException extends RuntimeException {

    private final SourcePosition position;
    private final String tagName;

    /**
     * @param message what is wrong, for the author
     */
    public TagException(String message) {
        this(message, (TagElement) null, null, null);
    }

    /**
     * @param message what is wrong, for the author
     * @param element the element it is about; {@code null} if none
     */
    public TagException(String message, TagElement element) {
        this(message, element, (String) null, null);
    }

    /**
     * @param message what is wrong, for the author
     * @param element the element it is about; {@code null} if none
     * @param cause   the exception that made the tag fail; {@code null} if none
     */
    public TagException(String message, TagElement element, Throwable cause) {
        this(message, element, (String) null, cause);
    }

    /**
     * For an error about one attribute: the position is the attribute's (else the element's).
     *
     * @param message   what is wrong, for the author
     * @param element   the element it is about; {@code null} if none
     * @param attribute the attribute whose position to give; {@code null} or {@code ""} for the element's
     * @param cause     the exception that made the tag fail; {@code null} if none
     */
    protected TagException(String message, TagElement element, String attribute, Throwable cause) {
        this(message, positionOf(element, attribute), element == null ? "" : element.name(), cause);
    }

    private TagException(String message, SourcePosition position, String tagName, Throwable cause) {
        super(position.isKnown() ? message + " (" + position.describe() + ")" : message, cause);
        this.position = position;
        this.tagName = tagName == null ? "" : tagName;
    }

    private static SourcePosition positionOf(TagElement element, String attribute) {
        if (element == null) return SourcePosition.UNKNOWN;
        SourcePosition position = attribute == null || attribute.isEmpty()
                ? element.position() : element.position(attribute);
        return position == null ? SourcePosition.UNKNOWN : position;
    }

    /**
     * @return where the error is; {@link SourcePosition#UNKNOWN} if unknown. {@link #getMessage()} already ends with a
     *         known position
     */
    public SourcePosition position() {
        return position;
    }

    /**
     * @return the name of the element the error is about, as written (with a pack's prefix); {@code ""} if none was
     *         given
     */
    public String tagName() {
        return tagName;
    }
}
