package com.zarterstein.magid.api.morphers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a {@link MagidTagMorpherInterface} implementation as a tag morpher and names the tags it rewrites: it turns a
 * MagidML shorthand tag (e.g. {@code <fn:x>}, {@code <_.a.b>}) into its canonical form while the story loads, before
 * any tag processor runs. The engine discovers annotated classes; never register them by hand.
 *
 * <pre>{@code
 * @MagidTagNameMorpher(target = "^fn:*, ^fn.*")
 * public class FnMorpher implements MagidTagMorpherInterface { … }
 * }</pre>
 *
 * @author Mark-Marion Gondelman (Creator)
 * @author JavaDocs: Claude Code (Model: claude-opus-5-5)
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MagidTagNameMorpher {
    /**
     * The tag-name patterns the morpher handles, comma-separated: {@code ^prefix*} (starts with), {@code ^exact$}
     * (exactly), {@code *suffix$} (ends with), or a plain name (exactly).
     *
     * @return the patterns
     */
    String target();
}
