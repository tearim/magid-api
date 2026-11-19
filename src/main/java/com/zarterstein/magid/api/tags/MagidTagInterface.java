package com.zarterstein.magid.api.tags;

import com.zarterstein.magid.api.som.StoryObjectModel;
import com.zarterstein.magid.api.values.BasicTypedValue;

import java.util.Map;

/**
 * A MagidML tag, as the engine calls it: every tag that runs (core tags and tagpack tags alike; only the structural
 * tags such as {@code <scene>} are handled by the loader instead). Extend {@link MagidTag}, which implements all of
 * this over the {@link TagElement} and {@link TagContext} the engine binds; a tag then writes only
 * {@link #process(TagElement, String)} and, if it acts, {@link #execute()}.
 * <p>
 * <b>Lifecycle.</b> The engine creates a new instance for every use (public no-argument constructor), calls
 * {@link #bind(TagElement, TagContext)}, then <i>either</i> {@link #process()} (inside a narration block such as
 * {@code <description>}: the tag contributes text) <i>or</i> {@link #execute()} (inside an action: the tag acts and
 * says what happens next). An instance is never reused, so a tag may keep state in fields during one use, but
 * nothing between uses (keep that in symbols).
 * </p>
 * <p>
 * The interface is deliberately broad: besides what the engine calls, it carries the helpers every tag needs
 * (attribute values, the story model, narration), so they are at hand in any tag.
 * </p>
 *
 * <pre>{@code
 * @MagidXmlTag(target = "greet", types = {SemanticType.TEXT_GENERATOR})
 * public class GreetTag extends MagidTag {
 *     public Map<String, String> process(TagElement element, String context) {
 *         return TagProcessorResult.mapWithTextOnly("Hello, " + getAttributeAsString("name") + "!");
 *     }
 *     public boolean newLine()    { return false; }
 *     public boolean zeroLength() { return false; }
 * }
 * }</pre>
 *
 * @author Mark-Marion Gondelman (Creator)
 * @author Refactoring: Claude Code (agentic refactoring)
 * @author JavaDocs: Claude Code (Model: claude-opus-5-5)
 * @see MagidTag
 */
public interface MagidTagInterface {

    // ─── called by the engine ───────────────────────────────────────────────────────────────────────────────────

    /**
     * Gives the tag its element and its context. Called once, before {@link #process()} or {@link #execute()}.
     *
     * @param element the tag's element
     * @param context what the engine offers the tag while it runs
     */
    void bind(TagElement element, TagContext context);

    /**
     * Inside a narration block: what the tag contributes to the text. The map is one result: its {@code text} field,
     * and any further fields of a line (e.g. a text class). Build it with {@link TagProcessorResult#mapWithTextOnly}
     * or {@link TagProcessorResult#emptyMapWithTextField()}.
     *
     * @return the tag's result
     */
    Map<String, String> process();

    /**
     * {@link #process()} with the element and the block name given explicitly, as the engine's own tags write it.
     * {@link MagidTag#process()} calls it with the bound element and {@link #blockName()}.
     *
     * @param element the tag's element
     * @param context the block the tag stands in ({@link #blockName()}); {@code ""} or {@code null} if none
     * @return the tag's result
     */
    Map<String, String> process(TagElement element, String context);

    /**
     * Inside an action: acts, and says what happens next. Text is narrated with {@link #narrate(String)} (and its
     * siblings), not returned.
     *
     * @return what the story does next; {@link Outcome#proceed()} to go on with the next tag
     */
    Outcome execute();

    /**
     * Layout: whether the result of {@link #process()} joins the text before it, or starts a new result.
     *
     * @return {@code true} to join the text before
     */
    boolean doesStickToLastText();

    /**
     * Layout of the text that follows the tag: whether the tag ended a line. It decides whether a space is kept
     * before the next text, in every kind of session.
     *
     * @return {@code true} if the tag's output ended a line
     */
    boolean newLine();

    /**
     * Layout of the text that follows the tag: whether the tag produced nothing, so that whitespace around it isn't
     * doubled.
     *
     * @return {@code true} if the tag produced no text
     */
    boolean zeroLength();

    // ─── the tag's element ──────────────────────────────────────────────────────────────────────────────────────

    /**
     * The tag's name as the story wrote it, prefix included.
     *
     * @return e.g. {@code show} or {@code llm-response}
     */
    String getName();

    /**
     * The block the tag stands in: inside narration, the nearest narration block (e.g. {@code description});
     * inside an action, the parent element's name.
     *
     * @return the block's name; {@code ""} if none
     */
    String blockName();

    /**
     * An attribute's value; evaluated if the tag's attributes are dynamic ({@link #isDynamic()}) and expressions are
     * enabled, raw otherwise.
     *
     * @param name the attribute's name
     * @return the value; {@code ""} if the attribute is missing or evaluates to nothing, never {@code null}
     */
    Object getAttributeValue(String name);

    /**
     * @param name the attribute's name
     * @return {@link #getAttributeValue(String)} as a string; {@code ""} if the attribute is missing or evaluates to
     *         nothing, never {@code null}
     */
    String getAttributeAsString(String name);

    /**
     * An attribute as a condition: evaluated, then read as a boolean ({@code true}/{@code false}, any case). The raw,
     * unevaluated reading is {@link TagElement#isTrue(String, boolean)}.
     *
     * @param ofAttribute the attribute's name
     * @return {@code true} if the evaluated value is {@code true}; {@code false} if it is missing or anything else
     */
    boolean isTrue(String ofAttribute);

    /**
     * Whether the tag's output can change between two uses: the tag is a {@link SemanticType#DYNAMIC_GENERATOR}, or
     * its attributes or text contain expressions. Determined once per instance, so a static tag never pays for the
     * evaluator.
     *
     * @return {@code true} if the tag is dynamic
     */
    boolean isDynamic();

    // ─── the tag's declaration (@MagidXmlTag) ───────────────────────────────────────────────────────────────────

    /**
     * @return the names the tag's class is registered under, comma-separated ({@link MagidXmlTag#target()})
     */
    String getTargets();

    /**
     * @param type a type
     * @return whether the tag's class declares the type ({@link MagidXmlTag#types()})
     */
    boolean hasProcessingType(SemanticType type);

    // ─── the story ──────────────────────────────────────────────────────────────────────────────────────────────

    /**
     * @return the story model: scenes, choices, fragments, narration
     */
    StoryObjectModel som();

    /**
     * Narrates a line of text, with {@code {…}} substituted when expressions are enabled.
     *
     * @param text the text
     */
    void narrate(String text);

    /**
     * Narrates one line of named fields.
     *
     * @param fields the fields, in the order they are sent
     */
    void narrate(Map<String, ? extends BasicTypedValue<?>> fields);

    /**
     * Narrates each result as one line of fields, in order.
     *
     * @param result the results
     */
    void narrate(TagProcessorResultInterface result);
}
