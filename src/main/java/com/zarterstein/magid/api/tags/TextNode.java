package com.zarterstein.magid.api.tags;

import java.util.Objects;

/**
 * A piece of text among an element's children, raw (expressions not evaluated, whitespace as written).
 *
 * @param text the text; never {@code null}
 * @author Mark-Marion Gondelman (Orchestration)
 * @author Code: Claude Code (agentic implementation)
 */
public record TextNode(String text) implements TagNode {

    /**
     * @throws NullPointerException if {@code text} is {@code null}
     */
    public TextNode {
        Objects.requireNonNull(text, "text");
    }
}
