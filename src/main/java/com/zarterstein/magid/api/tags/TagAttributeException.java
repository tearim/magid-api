package com.zarterstein.magid.api.tags;

import java.util.Optional;

/**
 * A tag's attribute is missing, or its value is wrong.
 *
 * <pre>{@code
 * if (sides < 2) {
 *     throw new TagAttributeException("<roll sides=\"…\"> must be at least 2", "sides", position);
 * }
 * }</pre>
 *
 * @author Mark-Marion Gondelman (Orchestration)
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
     * @param message  what is wrong, for the author
     * @param position where (the element's position, as the attribute's name isn't given); {@code null} if unknown
     */
    public TagAttributeException(String message, SourcePosition position) {
        this(message, null, position);
    }

    /**
     * @param message   what is wrong, for the author
     * @param attribute the attribute's name; {@code null} if not given
     * @param position  where: the attribute's position if it is present, else its element's; {@code null} if unknown
     */
    public TagAttributeException(String message, String attribute, SourcePosition position) {
        super(message, position);
        this.attribute = attribute;
    }

    /**
     * @return the name of the missing or wrong attribute, if given
     */
    public Optional<String> attribute() {
        return Optional.ofNullable(attribute);
    }
}
