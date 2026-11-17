package com.zarterstein.magid.api.tags;

/**
 * A tag is used where it can't work: in the wrong parent, or in a block that can't run it.
 *
 * <pre>{@code
 * throw new TagContextException("<card> must be inside a <deck>", position);
 * }</pre>
 *
 * @author Mark-Marion Gondelman (Orchestration)
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
     * @param message  what is wrong, for the author
     * @param position where; {@link SourcePosition#UNKNOWN} (or {@code null}) if unknown
     */
    public TagContextException(String message, SourcePosition position) {
        super(message, position);
    }
}
