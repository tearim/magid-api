package com.zarterstein.magid.api.tags;

/**
 * A child of an element, as a tag sees it: another element ({@link TagElement}) or a piece of text
 * ({@link TextNode}). Comments and other markup are not shown.
 *
 * <pre>{@code
 * for (TagNode child : element.children()) {
 *     switch (child) {
 *         case TagElement inner -> …
 *         case TextNode text    -> …
 *     }
 * }
 * }</pre>
 *
 * @author Mark-Marion Gondelman (Orchestration)
 * @author Code: Claude Code (agentic implementation)
 */
public sealed interface TagNode permits TagElement, TextNode {
}
