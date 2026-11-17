package com.zarterstein.magid.api.tags;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The implementation of {@link TagProcessorResultInterface}, with the helpers that build single results. The
 * engine's own handler extends it (and adds its JSON form).
 *
 * <pre>{@code
 * TagProcessorResult result = TagProcessorResult.emptyText();
 * result.appendTextToTheLast("The Duke nods.");
 * result.addResult(TagProcessorResult.commenceHandling("text", "He leaves."));
 * narrate(result);                                  // two lines
 * }</pre>
 *
 * @author Mark-Marion Gondelman (Creator)
 * @author Refactoring: Claude Code (agentic refactoring)
 * @author JavaDocs: Claude Code (Model: claude-opus-5-5)
 */
public class TagProcessorResult implements TagProcessorResultInterface {

    /** The name of the field that holds a result's text. */
    public static final String TEXT = "text";

    /** The results, in order. */
    protected final List<Map<String, String>> results = new ArrayList<>();

    /** Creates an empty result list. */
    public TagProcessorResult() {}

    @Override
    public void addResult(Map<String, String> singleTagResult) {
        results.add(singleTagResult);
    }

    @Override
    public void appendTextToTheLast(String text) {
        results.getLast().put(TEXT, results.getLast().get(TEXT) + text);
    }

    @Override
    public void addFieldsToTheLastResult(Map<String, String> moreFields) {
        addFields(results.getLast(), moreFields);
    }

    @Override
    public void addFieldsToTheFirstResult(Map<String, String> moreFields) {
        if (moreFields.isEmpty()) return;
        if (results.isEmpty()) results.add(new HashMap<>());
        addFields(results.getFirst(), moreFields);
    }

    private static void addFields(Map<String, String> target, Map<String, String> moreFields) {
        for (Map.Entry<String, String> field : moreFields.entrySet()) {
            target.merge(field.getKey(), field.getValue(), String::concat);
        }
    }

    @Override
    public String getStringResult() {
        StringBuilder result = new StringBuilder();
        for (Map<String, String> intermediate : results) {
            result.append(intermediate.getOrDefault(TEXT, ""));
        }
        return result.toString();
    }

    @Override
    public List<Map<String, String>> getResults() {
        return results;
    }

    @Override
    public void replaceResult(List<Map<String, String>> result) {
        List<Map<String, String>> copy = new ArrayList<>(result);   // result may be the live list itself
        results.clear();
        results.addAll(copy);
    }

    @Override
    public boolean isEmpty() {
        return results.isEmpty();
    }

    @Override
    public boolean containsNoText() {
        return results.isEmpty() || (results.size() == 1 && results.getFirst().isEmpty());
    }

    // ─── single results ─────────────────────────────────────────────────────────────────────────────────────────

    /**
     * @param text a text
     * @return a new result with that text
     */
    public static Map<String, String> commenceHandling(String text) {
        return commenceHandling(TEXT, text);
    }

    /**
     * @param firstField the first field's name
     * @param firstVal   its value
     * @return a new result (ordered) with that field
     */
    public static Map<String, String> commenceHandling(String firstField, String firstVal) {
        Map<String, String> result = new LinkedHashMap<>();
        result.put(firstField, firstVal);
        return result;
    }

    /**
     * @return a new result with an empty text
     */
    public static Map<String, String> emptyMapWithTextField() {
        return commenceHandling("");
    }

    /**
     * @param text a text
     * @return a new result with only that text
     */
    public static Map<String, String> mapWithTextOnly(String text) {
        return commenceHandling(text);
    }

    // ─── result lists ───────────────────────────────────────────────────────────────────────────────────────────

    /**
     * @return a new list with no results
     */
    public static TagProcessorResult empty() {
        return new TagProcessorResult();
    }

    /**
     * @return a new list with one result whose text is empty
     */
    public static TagProcessorResult emptyText() {
        TagProcessorResult empty = new TagProcessorResult();
        empty.addResult(emptyMapWithTextField());
        return empty;
    }
}
