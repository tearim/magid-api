package com.zarterstein.magid.api.tags;

import java.util.Objects;

/**
 * A tag's attribute has a value the tag can't use. The message is written for you, and quotes the value as the author
 * wrote it (shortened):
 *
 * <pre>{@code
 * throw new BadTagAttributeException(element, "type", "expected one of: bool, int, enum");
 * // `olla-flag` has a bad `type` attribute (`banana`): expected one of: bool, int, enum (XML source …)
 * }</pre>
 *
 * @author Mark-Marion Gondelman (Design, Orchestration)
 * @author Code: Claude Code (agentic implementation)
 */
public class BadTagAttributeException extends TagAttributeException {

    /** Longer values are shortened in the message. */
    static final int MAX_QUOTED = 40;

    /**
     * @param element      the element
     * @param badAttribute the attribute's name
     * @throws NullPointerException if either is {@code null}
     */
    public BadTagAttributeException(TagElement element, String badAttribute) {
        this(element, badAttribute, null);
    }

    /**
     * @param element      the element
     * @param badAttribute the attribute's name
     * @param explanation  what is wrong, or what is expected; {@code null} or blank for none
     * @throws NullPointerException if the element or the attribute is {@code null}
     */
    public BadTagAttributeException(TagElement element, String badAttribute, String explanation) {
        super("`" + Objects.requireNonNull(element, "element").name() + "` has a bad `"
                        + Objects.requireNonNull(badAttribute, "badAttribute") + "` attribute"
                        + quoted(element, badAttribute) + explained(explanation),
                element, badAttribute, badAttribute);
    }

    /** @return {@code " (`value`)"}, the raw value shortened; {@code ""} if the element lacks the attribute */
    private static String quoted(TagElement element, String attribute) {
        if (!element.hasAttribute(attribute)) return "";
        String value = element.attribute(attribute);
        if (value == null) return "";
        if (value.length() > MAX_QUOTED) value = value.substring(0, MAX_QUOTED) + "…";
        return " (`" + value + "`)";
    }
}
