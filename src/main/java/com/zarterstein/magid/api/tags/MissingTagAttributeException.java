package com.zarterstein.magid.api.tags;

import java.util.Objects;

/**
 * A tag lacks an attribute it needs. The message is written for you:
 *
 * <pre>{@code
 * if (!element.hasAttribute("model")) {
 *     throw new MissingTagAttributeException(element, "model", "the model's name, e.g. llama3");
 * }
 * // `olla-config` needs a `model` attribute: the model's name, e.g. llama3 (XML source (story.xml): around line 12, column 4)
 * }</pre>
 *
 * @author Mark-Marion Gondelman (Design, Orchestration)
 * @author Code: Claude Code (agentic implementation)
 */
public class MissingTagAttributeException extends TagAttributeException {

    /**
     * @param element          the element that lacks the attribute
     * @param missingAttribute the attribute's name
     * @throws NullPointerException if either is {@code null}
     */
    public MissingTagAttributeException(TagElement element, String missingAttribute) {
        this(element, missingAttribute, null);
    }

    /**
     * @param element          the element that lacks the attribute
     * @param missingAttribute the attribute's name
     * @param explanation      what the attribute is for, or an example; {@code null} or blank for none
     * @throws NullPointerException if the element or the attribute is {@code null}
     */
    public MissingTagAttributeException(TagElement element, String missingAttribute, String explanation) {
        super("`" + Objects.requireNonNull(element, "element").name() + "` needs a `"
                        + Objects.requireNonNull(missingAttribute, "missingAttribute") + "` attribute"
                        + explained(explanation),
                element, missingAttribute, null);
    }
}
