package com.zarterstein.magid.api.tags;

/**
 * A tag's attribute is missing, or its value is wrong. For the usual cases, {@link MissingTagAttributeException} and
 * {@link BadTagAttributeException} write the message themselves; this class takes one of your own.
 *
 * <pre>{@code
 * if (sides < 2) {
 *     throw new TagAttributeException("a die needs at least 2 sides", element, "sides");
 * }
 * }</pre>
 *
 * @author Mark-Marion Gondelman (Design, Orchestration)
 * @author Code: Claude Code (agentic implementation)
 */
public class TagAttributeException extends TagException {

    private final String attribute;

    /**
     * @param message what is wrong, for the author
     */
    public TagAttributeException(String message) {
        this(message, null, null);
    }

    /**
     * @param message what is wrong, for the author
     * @param element the element; its position ends the message; {@code null} if none
     */
    public TagAttributeException(String message, TagElement element) {
        this(message, element, null);
    }

    /**
     * @param message   what is wrong, for the author
     * @param element   the element; {@code null} if none
     * @param attribute the attribute's name; its position (else the element's) ends the message; {@code null} or
     *                  {@code ""} if not given
     */
    public TagAttributeException(String message, TagElement element, String attribute) {
        this(message, element, attribute, attribute);
    }

    /**
     * @param message           what is wrong, for the author
     * @param element           the element; {@code null} if none
     * @param attribute         the attribute's name ({@link #attribute()})
     * @param positionAttribute the attribute whose position ends the message; {@code null} for the element's (a
     *                          missing attribute has no position of its own)
     */
    protected TagAttributeException(String message, TagElement element, String attribute, String positionAttribute) {
        super(message, element, positionAttribute, null);
        this.attribute = attribute == null ? "" : attribute;
    }

    /**
     * @return the name of the missing or wrong attribute; {@code ""} if not given
     */
    public String attribute() {
        return attribute;
    }

    /** @return {@code ": " + explanation}, or {@code ""} for none */
    static String explained(String explanation) {
        return explanation == null || explanation.isBlank() ? "" : ": " + explanation.strip();
    }
}
