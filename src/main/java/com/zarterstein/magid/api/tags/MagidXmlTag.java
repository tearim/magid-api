package com.zarterstein.magid.api.tags;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class as a MagidML tag processor and names the tags it handles. The engine discovers annotated classes and
 * registers them; never register them by hand.
 *
 * <pre>{@code
 * @MagidXmlTag(target = "stop, wait, pause", types = {SemanticType.TEXT_GENERATOR})
 * public class StopTag extends XmlSingleTagProcessor { … }
 * }</pre>
 *
 * @author Mark-Marion Gondelman (Creator)
 * @author JavaDocs: Claude Code (Model: claude-opus-5-5)
 * @see SemanticType
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MagidXmlTag {
    /**
     * The tag names the class handles, comma-separated (aliases), e.g. {@code "stop, wait, pause"}. Empty: the class
     * must provide a {@code registerInTagProcessorFactory()} method, which discovery calls on a new instance.
     *
     * @return the tag names, or {@code ""}
     */
    String target() default "";
    /**
     * The semantic types of every name in {@link #target()}.
     *
     * @return the types; {@link SemanticType#GENERIC} by default
     */
    SemanticType[] types() default {SemanticType.GENERIC};
}
