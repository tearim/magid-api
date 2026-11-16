package com.zarterstein.magid.api.tags;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link SourcePosition}, {@link TagException} and its subclasses: the message ends with the position when there is
 * one.
 *
 * @author Mark-Marion Gondelman (Orchestration)
 * @author Code: Claude Code (agentic implementation)
 */
class TagExceptionTest {

    private static final SourcePosition AT = new SourcePosition("dice.xml", "XML", 12, 4);

    // ─── SourcePosition ─────────────────────────────────────────────────────

    @Test
    void positionDescribesItself() {
        assertEquals("XML source (dice.xml): around line 12, column 4", AT.describe());
    }

    @Test
    void lineBelowOneIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new SourcePosition("dice.xml", "XML", 0, 1));
    }

    @Test
    void columnBelowOneIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new SourcePosition("dice.xml", "XML", 1, 0));
    }

    @Test
    void nullFileIsRejected() {
        assertThrows(NullPointerException.class, () -> new SourcePosition(null, "XML", 1, 1));
    }

    @Test
    void nullFileTypeIsRejected() {
        assertThrows(NullPointerException.class, () -> new SourcePosition("dice.xml", null, 1, 1));
    }

    // ─── TagException ───────────────────────────────────────────────────────

    @Test
    void messageWithoutPositionIsUnchanged() {
        TagException e = new TagException("Bad roll");
        assertAll(
                () -> assertEquals("Bad roll", e.getMessage()),
                () -> assertEquals(Optional.empty(), e.position()));
    }

    @Test
    void messageEndsWithThePosition() {
        TagException e = new TagException("Bad roll", AT);
        assertAll(
                () -> assertEquals("Bad roll (XML source (dice.xml): around line 12, column 4)", e.getMessage()),
                () -> assertEquals(Optional.of(AT), e.position()));
    }

    @Test
    void nullPositionIsNoPosition() {
        TagException e = new TagException("Bad roll", null);
        assertAll(
                () -> assertEquals("Bad roll", e.getMessage()),
                () -> assertEquals(Optional.empty(), e.position()));
    }

    @Test
    void causeIsKept() {
        IllegalStateException cause = new IllegalStateException("no dice");
        assertSame(cause, new TagException("Bad roll", AT, cause).getCause());
    }

    @Test
    void tagExceptionIsUnchecked() {
        assertInstanceOf(RuntimeException.class, new TagException("Bad roll"));
    }

    // ─── subclasses ─────────────────────────────────────────────────────────

    @Test
    void attributeExceptionNamesTheAttribute() {
        TagAttributeException e = new TagAttributeException("Bad sides", "sides", AT);
        assertAll(
                () -> assertEquals(Optional.of("sides"), e.attribute()),
                () -> assertEquals(Optional.of(AT), e.position()),
                () -> assertTrue(e.getMessage().endsWith("around line 12, column 4)"), e.getMessage()));
    }

    @Test
    void attributeExceptionWithoutAttributeName() {
        TagAttributeException e = new TagAttributeException("Bad sides", AT);
        assertAll(
                () -> assertEquals(Optional.empty(), e.attribute()),
                () -> assertEquals(Optional.of(AT), e.position()));
    }

    @Test
    void contextExceptionCarriesThePosition() {
        TagContextException e = new TagContextException("<card> must be inside a <deck>", AT);
        assertAll(
                () -> assertInstanceOf(TagException.class, e),
                () -> assertEquals(Optional.of(AT), e.position()));
    }
}
