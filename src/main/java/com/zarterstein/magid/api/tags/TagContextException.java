package com.zarterstein.magid.api.tags;

/**
 * A tag is used where it can't work: in the wrong parent, or in a block that can't run it. For a tag that must be
 * inside another, {@link MisplacedTagException} writes the message for you.
 *
 * <pre>{@code
 * throw new TagContextException("a card can't be dealt in a description", element);
 * }</pre>
 *
 * @author Mark-Marion Gondelman (Design, Orchestration)
 * @author Code: Claude Code (agentic implementation)
 */
public class TagContextException extends TagException {

    /**
     * @param message what is wrong, for the author
     */
    public TagContextException(String message) {
        super(message);
    }

    /**
     * @param message what is wrong, for the author
     * @param element the misplaced element; its position ends the message; {@code null} if none
     */
    public TagContextException(String message, TagElement element) {
        super(message, element);
    }

    /**
     * @param message   what is wrong, for the author
     * @param element   the misplaced element; {@code null} if none
     * @param attribute the attribute whose position ends the message (else the element's); {@code null} or
     *                  {@code ""} for the element's
     */
    protected TagContextException(String message, TagElement element, String attribute) {
        super(message, element, attribute, null);
    }
}
