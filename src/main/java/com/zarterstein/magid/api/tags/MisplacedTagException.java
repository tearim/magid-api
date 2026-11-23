package com.zarterstein.magid.api.tags;

import java.util.Objects;

/**
 * A tag that must be inside another one isn't. The message is written for you:
 *
 * <pre>{@code
 * throw new MisplacedTagException(element, "olla-select", "description");
 * // `olla-option` must be inside `olla-select`, not inside `description` (XML source …)
 * }</pre>
 *
 * @author Mark-Marion Gondelman (Design, Orchestration)
 * @author Code: Claude Code (agentic implementation)
 */
public class MisplacedTagException extends TagContextException {

    /**
     * @param element   the misplaced element
     * @param allowedIn where it belongs, e.g. {@code olla-select} or {@code an action}
     * @throws NullPointerException if either is {@code null}
     */
    public MisplacedTagException(TagElement element, String allowedIn) {
        this(element, allowedIn, null);
    }

    /**
     * @param element   the misplaced element
     * @param allowedIn where it belongs, e.g. {@code olla-select} or {@code an action}
     * @param foundIn   where it is, e.g. {@code description}; {@code null} or blank if unknown
     * @throws NullPointerException if the element or {@code allowedIn} is {@code null}
     */
    public MisplacedTagException(TagElement element, String allowedIn, String foundIn) {
        super("`" + Objects.requireNonNull(element, "element").name() + "` must be inside `"
                + Objects.requireNonNull(allowedIn, "allowedIn") + "`"
                + (foundIn == null || foundIn.isBlank() ? "" : ", not inside `" + foundIn.strip() + "`"), element);
    }
}
