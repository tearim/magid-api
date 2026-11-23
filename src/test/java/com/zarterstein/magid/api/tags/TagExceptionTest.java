package com.zarterstein.magid.api.tags;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link SourcePosition}, {@link TagException} and its subclasses: the message ends with the element's position when
 * it has one; the attribute exceptions and {@link MisplacedTagException} write their messages themselves.
 *
 * @author Mark-Marion Gondelman (Orchestration)
 * @author Code: Claude Code (agentic implementation)
 */
class TagExceptionTest {

    private static final SourcePosition AT = new SourcePosition("dice.xml", "XML", 12, 4);
    private static final SourcePosition AT_SIDES = new SourcePosition("dice.xml", "XML", 12, 10);
    private static final String WHERE = " (XML source (dice.xml): around line 12, column 4)";

    /** A {@code <dice-roll>} at {@link #AT}; its attributes are at {@link #AT_SIDES}. */
    private record Roll(Map<String, String> attributes) implements TagElement {
        @Override public String name() { return "dice-roll"; }
        @Override public String localName() { return "roll"; }
        @Override public String attribute(String n) { return attributes.getOrDefault(n, ""); }
        @Override public boolean hasAttribute(String n) { return attributes.containsKey(n); }
        @Override public Set<String> attributeNames() { return attributes.keySet(); }
        @Override public boolean isTrue(String n, boolean fallback) { return fallback; }
        @Override public String text() { return ""; }
        @Override public List<TagNode> children() { return List.of(); }
        @Override public SourcePosition position() { return AT; }
        @Override public SourcePosition position(String attribute) { return hasAttribute(attribute) ? AT_SIDES : AT; }
    }

    private static final Roll ROLL = new Roll(Map.of("sides", "banana"));
    private static final Roll BARE_ROLL = new Roll(Map.of());

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

    @Test
    void unknownPositionIsNotKnown() {
        assertAll(
                () -> assertFalse(SourcePosition.UNKNOWN.isKnown()),
                () -> assertTrue(AT.isKnown()),
                () -> assertEquals("unknown position", SourcePosition.UNKNOWN.describe()));
    }

    @Test
    void onlyBothMinusOneMeanUnknown() {
        assertThrows(IllegalArgumentException.class, () -> new SourcePosition("dice.xml", "XML", -1, 4));
    }

    // ─── TagException ───────────────────────────────────────────────────────

    @Test
    void messageWithoutAnElementIsUnchanged() {
        assertEquals("Bad roll", new TagException("Bad roll").getMessage());
    }

    @Test
    void withoutAnElementThePositionIsUnknown() {
        assertEquals(SourcePosition.UNKNOWN, new TagException("Bad roll").position());
    }

    @Test
    void withoutAnElementTheTagNameIsEmpty() {
        assertEquals("", new TagException("Bad roll").tagName());
    }

    @Test
    void messageEndsWithTheElementsPosition() {
        assertEquals("Bad roll" + WHERE, new TagException("Bad roll", ROLL).getMessage());
    }

    @Test
    void thePositionIsTheElements() {
        assertEquals(AT, new TagException("Bad roll", ROLL).position());
    }

    @Test
    void theTagNameIsTheElementsAsWritten() {
        assertEquals("dice-roll", new TagException("Bad roll", ROLL).tagName());
    }

    @Test
    void aNullElementIsNoElement() {
        assertEquals("Bad roll", new TagException("Bad roll", (TagElement) null).getMessage());
    }

    @Test
    void causeIsKept() {
        IllegalStateException cause = new IllegalStateException("no dice");
        assertSame(cause, new TagException("Bad roll", ROLL, cause).getCause());
    }

    @Test
    void tagExceptionIsUnchecked() {
        assertInstanceOf(RuntimeException.class, new TagException("Bad roll"));
    }

    // ─── TagAttributeException ──────────────────────────────────────────────

    @Test
    void attributeExceptionNamesTheAttribute() {
        assertEquals("sides", new TagAttributeException("Bad sides", ROLL, "sides").attribute());
    }

    @Test
    void attributeExceptionHasTheAttributesPosition() {
        assertEquals(AT_SIDES, new TagAttributeException("Bad sides", ROLL, "sides").position());
    }

    @Test
    void attributeExceptionWithoutAttributeHasTheElementsPosition() {
        assertEquals(AT, new TagAttributeException("Bad sides", ROLL).position());
    }

    @Test
    void attributeExceptionWithoutAttributeNamesNone() {
        assertEquals("", new TagAttributeException("Bad sides", ROLL).attribute());
    }

    // ─── MissingTagAttributeException ───────────────────────────────────────

    @Test
    void missingAttributeWritesTheMessage() {
        assertEquals("`dice-roll` needs a `sides` attribute" + WHERE,
                new MissingTagAttributeException(BARE_ROLL, "sides").getMessage());
    }

    @Test
    void missingAttributeAddsTheExplanation() {
        assertEquals("`dice-roll` needs a `sides` attribute: how many sides the die has" + WHERE,
                new MissingTagAttributeException(BARE_ROLL, "sides", "how many sides the die has").getMessage());
    }

    @Test
    void missingAttributeWithABlankExplanationAddsNothing() {
        assertEquals("`dice-roll` needs a `sides` attribute" + WHERE,
                new MissingTagAttributeException(BARE_ROLL, "sides", "  ").getMessage());
    }

    @Test
    void missingAttributeNamesTheAttribute() {
        assertEquals("sides", new MissingTagAttributeException(BARE_ROLL, "sides").attribute());
    }

    @Test
    void missingAttributeIsAnAttributeException() {
        assertInstanceOf(TagAttributeException.class, new MissingTagAttributeException(BARE_ROLL, "sides"));
    }

    @Test
    void missingAttributeNeedsAnElement() {
        assertThrows(NullPointerException.class, () -> new MissingTagAttributeException(null, "sides"));
    }

    // ─── BadTagAttributeException ───────────────────────────────────────────

    @Test
    void badAttributeQuotesTheValue() {
        assertEquals("`dice-roll` has a bad `sides` attribute (`banana`) (XML source (dice.xml): around line 12, column 10)",
                new BadTagAttributeException(ROLL, "sides").getMessage());
    }

    @Test
    void badAttributeAddsTheExplanation() {
        String message = new BadTagAttributeException(ROLL, "sides", "a whole number, at least 2").getMessage();
        assertTrue(message.startsWith("`dice-roll` has a bad `sides` attribute (`banana`): a whole number, at least 2 ("),
                message);
    }

    @Test
    void badAttributeHasTheAttributesPosition() {
        assertEquals(AT_SIDES, new BadTagAttributeException(ROLL, "sides").position());
    }

    @Test
    void badAttributeShortensALongValue() {
        Roll roll = new Roll(Map.of("sides", "x".repeat(100)));
        String message = new BadTagAttributeException(roll, "sides").getMessage();
        assertTrue(message.contains("(`" + "x".repeat(BadTagAttributeException.MAX_QUOTED) + "…`)"), message);
    }

    @Test
    void badAttributeThatIsAbsentQuotesNothing() {
        assertEquals("`dice-roll` has a bad `sides` attribute" + WHERE,
                new BadTagAttributeException(BARE_ROLL, "sides").getMessage());
    }

    // ─── context ────────────────────────────────────────────────────────────

    @Test
    void contextExceptionHasTheElementsPosition() {
        assertEquals(AT, new TagContextException("not here", ROLL).position());
    }

    @Test
    void misplacedTagSaysWhereItBelongs() {
        assertEquals("`dice-roll` must be inside `dice-cup`" + WHERE,
                new MisplacedTagException(ROLL, "dice-cup").getMessage());
    }

    @Test
    void misplacedTagSaysWhereItIs() {
        assertEquals("`dice-roll` must be inside `dice-cup`, not inside `description`" + WHERE,
                new MisplacedTagException(ROLL, "dice-cup", "description").getMessage());
    }

    @Test
    void misplacedTagIsAContextException() {
        assertInstanceOf(TagContextException.class, new MisplacedTagException(ROLL, "dice-cup"));
    }
}
