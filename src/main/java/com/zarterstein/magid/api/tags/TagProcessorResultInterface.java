package com.zarterstein.magid.api.tags;

import java.util.List;
import java.util.Map;

/**
 * The text a tag (or a block of tags) produced, as a list of results. Each result is one line of fields, with the
 * text under {@code text}; further fields (e.g. a text class) travel with it. Results are joined into the narration
 * in order. {@link TagProcessorResult} is the implementation to use.
 *
 * @author Mark-Marion Gondelman (Creator)
 * @author JavaDocs: Claude Code (Model: claude-opus-5-5)
 */
public interface TagProcessorResultInterface {

    /**
     * Adds a result after the others.
     *
     * @param singleTagResult the result's fields
     */
    void addResult(Map<String, String> singleTagResult);

    /**
     * Appends text to the {@code text} field of the last result.
     *
     * @param text the text
     * @throws java.util.NoSuchElementException if there is no result
     */
    void appendTextToTheLast(String text);

    /**
     * Adds fields to the last result; a field it already has gets the new value appended.
     *
     * @param moreFields the fields
     * @throws java.util.NoSuchElementException if there is no result
     */
    void addFieldsToTheLastResult(Map<String, String> moreFields);

    /**
     * Adds fields to the first result (created if there is none); a field it already has gets the new value
     * appended.
     *
     * @param moreFields the fields
     */
    void addFieldsToTheFirstResult(Map<String, String> moreFields);

    /**
     * @return the {@code text} fields of all results, joined
     */
    String getStringResult();

    /**
     * @return the results, in order (the live list)
     */
    List<Map<String, String>> getResults();

    /**
     * Replaces all results.
     *
     * @param result the new results
     */
    void replaceResult(List<Map<String, String>> result);

    /**
     * @return whether there are no results
     */
    boolean isEmpty();

    /**
     * @return whether there is no text: no results, or a single empty one
     */
    boolean containsNoText();
}
