package com.zarterstein.magid.api.morphers;

import org.w3c.dom.Element;

/**
 * A tag morpher: it rewrites a MagidML shorthand tag into its canonical form (for example {@code <fn:x>} into
 * {@code <call name="x">}). The engine applies every applicable morpher to every element while a story loads, before
 * any tag runs. Implementations carry {@link MagidTagNameMorpher}, which names the tags they handle.
 * <p>
 * A morpher works on the loading story's DOM and may change it (rename the element, set attributes). That is the
 * only place where this API exposes the DOM; tags themselves see a read-only view.
 * </p>
 *
 * @author Mark-Marion Gondelman (Creator)
 * @author JavaDocs: Claude Code (Model: claude-opus-5-5)
 */
public interface MagidTagMorpherInterface {
    /**
     * Transforms the given XML element.
     * This method is responsible for renaming the node and mapping shortcut attributes to their canonical forms.
     *
     * @param el The XML element to transform.
     */
    void transform(Element el);

    /**
     * Checks if this morpher is applicable to the given tag name.
     *
     * @param tag The tag name to check.
     * @return {@code true} if the morpher can handle this tag, {@code false} otherwise.
     */
    boolean isApplicable(String tag);
}
