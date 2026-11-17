package com.zarterstein.magid.api.tags;

import java.util.List;
import java.util.Set;

/**
 * A tag's element, read-only: its name, attributes, text and children, and where it stands in the story's source.
 * The engine's XML representation stays hidden, so a tag doesn't depend on it.
 * <p>
 * No method returns {@code null}: a missing attribute reads as {@code ""} (check with {@link #hasAttribute}), and
 * a position the engine doesn't know is a {@link SourcePosition} that says so.
 * </p>
 *
 * @author Mark-Marion Gondelman (Creator)
 * @author Refactoring: Claude Code (agentic refactoring)
 * @author JavaDocs: Claude Code (Model: claude-opus-5-5)
 */
public non-sealed interface TagElement extends TagNode {

    /**
     * @return the name as the story wrote it, prefix included, e.g. {@code llm-response}
     */
    String name();

    /**
     * @return the name without the pack's prefix, e.g. {@code response}; for core tags the same as {@link #name()}
     */
    String localName();

    /**
     * @param localName a local name
     * @return whether this element has that local name
     */
    default boolean isTag(String localName) {
        return localName().equals(localName);
    }

    /**
     * @param name an attribute's name
     * @return its raw value (expressions not evaluated); {@code ""} if the attribute is missing
     */
    String attribute(String name);

    /**
     * @param name an attribute's name
     * @return whether the element has the attribute
     */
    boolean hasAttribute(String name);

    /**
     * @return the names of the element's attributes, in no particular order
     */
    Set<String> attributeNames();

    /**
     * An attribute as a boolean, read raw (not evaluated): {@code true} (any case) is true, anything else false, as
     * {@link MagidTagInterface#isTrue(String)} reads the evaluated value.
     *
     * @param name     an attribute's name
     * @param fallback the answer when the attribute is missing
     * @return whether the attribute is {@code true}
     */
    boolean isTrue(String name, boolean fallback);

    /**
     * @return the element's text content, raw: the text of all its descendants, without their markup
     */
    String text();

    /**
     * @return the element's children, elements and text, in order
     */
    List<TagNode> children();

    /**
     * @return where the element's start tag stands in the story's source
     */
    SourcePosition position();

    /**
     * @param attribute an attribute's name
     * @return where the attribute stands; the element's position if the attribute is missing
     */
    SourcePosition position(String attribute);
}
