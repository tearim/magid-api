package com.zarterstein.magid.api.tags;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link TagProcessorResult}: results as lines of fields, joined in order.
 *
 * @author Mark-Marion Gondelman (Orchestration)
 * @author Code: Claude Code (agentic implementation)
 */
class TagProcessorResultTest {

    @Test
    void textIsAppendedToTheLastResult() {
        TagProcessorResult result = TagProcessorResult.emptyText();
        result.appendTextToTheLast("Hello");
        result.appendTextToTheLast(", world");
        assertEquals("Hello, world", result.getStringResult());
    }

    @Test
    void stringResultJoinsTheTextOfAllResults() {
        TagProcessorResult result = TagProcessorResult.empty();
        result.addResult(TagProcessorResult.mapWithTextOnly("One. "));
        result.addResult(TagProcessorResult.commenceHandling("class", "loud"));
        result.addResult(TagProcessorResult.mapWithTextOnly("Two."));
        assertEquals("One. Two.", result.getStringResult());
    }

    @Test
    void fieldsAreAppendedToAnExistingField() {
        TagProcessorResult result = TagProcessorResult.emptyText();
        result.addFieldsToTheLastResult(Map.of("text", "a", "class", "x"));
        result.addFieldsToTheLastResult(Map.of("class", "y"));
        assertEquals(Map.of("text", "a", "class", "xy"), result.getResults().getLast());
    }

    @Test
    void fieldsForTheFirstResultCreateIt() {
        TagProcessorResult result = TagProcessorResult.empty();
        result.addFieldsToTheFirstResult(Map.of("class", "x"));
        assertEquals(List.of(Map.of("class", "x")), result.getResults());
    }

    @Test
    void noFieldsForTheFirstResultCreateNothing() {
        TagProcessorResult result = TagProcessorResult.empty();
        result.addFieldsToTheFirstResult(Map.of());
        assertTrue(result.isEmpty());
    }

    @Test
    void appendingWithoutResultsThrows() {
        assertThrows(java.util.NoSuchElementException.class,
                () -> TagProcessorResult.empty().appendTextToTheLast("x"));
    }

    @Test
    void emptyAndEmptyText() {
        assertAll(
                () -> assertTrue(TagProcessorResult.empty().isEmpty()),
                () -> assertTrue(TagProcessorResult.empty().containsNoText()),
                () -> assertFalse(TagProcessorResult.emptyText().isEmpty()),
                () -> assertEquals("", TagProcessorResult.emptyText().getStringResult()));
    }

    @Test
    void replaceResultAcceptsItsOwnList() {
        TagProcessorResult result = TagProcessorResult.empty();
        result.addResult(TagProcessorResult.mapWithTextOnly("kept"));
        result.replaceResult(result.getResults());
        assertEquals("kept", result.getStringResult());
    }

    @Test
    void singleResultsKeepTheirFieldOrder() {
        Map<String, String> line = TagProcessorResult.commenceHandling("text", "a");
        line.put("class", "b");
        line.put("style", "c");
        assertEquals(List.of("text", "class", "style"), List.copyOf(line.keySet()));
    }
}
