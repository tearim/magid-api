package com.zarterstein.magid.api.tags;

/**
 * The semantic types of a MagidML tag, declared in {@link MagidXmlTag#types()}. The engine asks a tag's types to
 * decide how to run it. A tag may have several types.
 *
 * @author Mark-Marion Gondelman (Creator)
 * @author JavaDocs: Claude Code (Model: claude-opus-5-5)
 * @see MagidXmlTag
 */
public enum SemanticType {
    /** No particular type; the default for tags registered without types. */
    GENERIC,
    /**
     * The tag may produce text: the engine processes it when it renders narratable text.
     * Example: {@code <br>}, {@code <if>}.
     */
    TEXT_GENERATOR,
    /**
     * The tag's attributes and body are evaluated against the current state (symbols and expressions) every time.
     * Example: {@code <call>}, {@code <declare>}.
     */
    DYNAMIC_GENERATOR,
    /** The tag has a condition and runs its body by it. Example: {@code <if>}, {@code <while>}. */
    CONTROL_STRUCTURE,
    /** The tag talks to the web. Example: {@code <url>}, {@code <fetch>}. */
    WEB,
    /**
     * The tag opens a textual space where inline content, such as detached elements placed by anchor, lives.
     * Example: {@code <show>}, {@code <description>}.
     */
    NARRATION_BLOCK,

    /**
     * The tag is one of the engine's basic HTML tags, allowed by the server's configuration
     * ({@code magid.basic-html-tags.allow-list}).
     */
    ALLOWED_HTML;

    /**
     * The types a tag registered without types gets.
     *
     * @return a new array holding {@link #GENERIC}
     */
    public static SemanticType[] defaultValue() {
        return new SemanticType[]{SemanticType.GENERIC};
    }

}
