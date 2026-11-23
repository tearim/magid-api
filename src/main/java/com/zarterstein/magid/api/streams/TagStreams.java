package com.zarterstein.magid.api.streams;

/**
 * Opens {@link TagStream}s for a tag: {@link com.zarterstein.magid.api.tags.TagContext#streams()}.
 *
 * @author Mark-Marion Gondelman (Design, Orchestration)
 * @author Code: Claude Code (agentic implementation)
 */
@FunctionalInterface
public interface TagStreams {

    /** The streams of a context that can't stream: every stream is {@link TagStream#NONE}. */
    TagStreams NONE = () -> TagStream.NONE;

    /**
     * @return a new, open stream; {@link TagStream#NONE} if the client doesn't take streams or the session has as many
     *         open streams as it may: then the tag computes its whole text
     */
    TagStream open();
}
