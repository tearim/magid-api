package com.zarterstein.magid.api.streams;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link TagStream#NONE}, {@link TagStreams#NONE} and the placeholder.
 *
 * @author Mark-Marion Gondelman (Orchestration)
 * @author Code: Claude Code (agentic implementation)
 */
class TagStreamTest {

    @Test
    void theNoneStreamsOpenTheNoneStream() {
        assertSame(TagStream.NONE, TagStreams.NONE.open());
    }

    @Test
    void theNoneStreamIsNotOpen() {
        assertFalse(TagStream.NONE.isOpen());
    }

    @Test
    void theNoneStreamHasNoId() {
        assertEquals("", TagStream.NONE.id());
    }

    @Test
    void theNoneStreamHasNoPlaceholder() {
        assertEquals("", TagStream.NONE.placeholder());
    }

    @Test
    void aPlaceholderNamesTheStream() {
        assertEquals("⟪stream:str-1⟫", TagStream.placeholderOf("str-1"));
    }

    @Test
    void aStreamsPlaceholderIsItsIdsPlaceholder() {
        TagStream stream = new TagStream() {
            public String id() { return "str-7"; }
            public boolean isOpen() { return true; }
            public boolean isCancelled() { return false; }
            public void append(String text) { }
            public void done(String wholeText) { }
            public void fail(String fallback) { }
            public void warmUp(String text) { }
        };

        assertEquals("⟪stream:str-7⟫", stream.placeholder());
    }
}
