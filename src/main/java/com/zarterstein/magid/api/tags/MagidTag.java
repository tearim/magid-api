package com.zarterstein.magid.api.tags;

import com.zarterstein.magid.api.som.NarrationValue;
import com.zarterstein.magid.api.som.StoryObjectModel;
import com.zarterstein.magid.api.values.BasicTypedValue;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * The first ancestor of every tag that runs, core tags and tagpack tags alike. It implements
 * {@link MagidTagInterface} over the {@link TagElement} and {@link TagContext} the engine binds, so a tag writes
 * only what is its own: {@link #process(TagElement, String)}, the layout ({@link #newLine()}, {@link #zeroLength()})
 * and, if it acts, {@link #execute()}.
 * <p>
 * Defaults: {@link #execute()} calls {@link #process()} and goes on; a result starts a new record unless
 * {@link #appendToLastText} is set. Dynamicity is determined once, when the tag is bound
 * ({@link #determineDynamicity()}): the attributes of a static tag are returned raw, without calling the evaluator.
 * The engine's own tags extend this class through the engine's {@code XmlSingleTagProcessor}, which adds the engine's
 * internals; a tagpack's tags extend it directly and see only this API.
 * </p>
 *
 * @author Mark-Marion Gondelman (Orchestration)
 * @author Code: Claude Code (agentic implementation)
 * @see MagidTagInterface
 */
public abstract class MagidTag implements MagidTagInterface {

    /** The tag's element; set by {@link #bind}. */
    protected TagElement element;
    /**
     * What the engine offers while the tag runs; set by {@link #bind}. Not called {@code context}: the engine's own
     * tags use that name for the block name.
     */
    protected TagContext tagContext;
    /** Layout: whether {@link #process()}'s result joins the text before it ({@link #doesStickToLastText()}). */
    protected boolean appendToLastText = false;

    /** Whether the tag's text has expressions (or the tag is a {@link SemanticType#DYNAMIC_GENERATOR}). */
    protected boolean bodyDynamic = false;
    /** Whether the tag's attributes have expressions (or the tag is a {@link SemanticType#DYNAMIC_GENERATOR}). */
    protected boolean attributesDynamic = false;
    /** Whether {@link #determineDynamicity()} has run. */
    protected boolean dynamicityDetermined = false;

    /** For the engine's registry: tags are created with their no-argument constructor. */
    protected MagidTag() {}

    // ─── called by the engine ───────────────────────────────────────────────────────────────────────────────────

    /**
     * {@inheritDoc}
     * <p>
     * Also determines the tag's dynamicity ({@link #determineDynamicity()}).
     * </p>
     *
     * @throws NullPointerException if {@code element} or {@code context} is {@code null}
     */
    @Override
    public void bind(TagElement element, TagContext context) {
        this.element = Objects.requireNonNull(element, "element");
        this.tagContext = Objects.requireNonNull(context, "context");
        determineDynamicity();
    }

    /** {@inheritDoc} Calls {@link #process(TagElement, String)} with the bound element and {@link #blockName()}. */
    @Override
    public Map<String, String> process() {
        return process(element, blockName());
    }

    /**
     * {@inheritDoc}
     * <p>
     * Default: calls {@link #process()}, ignores its result, and goes on. Override it for tags that act.
     * </p>
     */
    @Override
    public Outcome execute() {
        process();
        return Outcome.proceed();
    }

    /** {@inheritDoc} Default: {@link #appendToLastText}. */
    @Override
    public boolean doesStickToLastText() {
        return appendToLastText;
    }

    // ─── the tag's element ──────────────────────────────────────────────────────────────────────────────────────

    @Override
    public String getName() {
        return element.name();
    }

    @Override
    public String blockName() {
        return tagContext.blockName();
    }

    /**
     * Determines, once, whether the tag is dynamic: a {@link SemanticType#DYNAMIC_GENERATOR} is dynamic in its text
     * and its attributes; otherwise its attributes are dynamic if any of them has expressions, and its text if the
     * text has. Later calls do nothing.
     */
    public void determineDynamicity() {
        if (dynamicityDetermined) return;
        if (hasProcessingType(SemanticType.DYNAMIC_GENERATOR)) {
            bodyDynamic = true;
            attributesDynamic = true;
        } else {
            for (String name : element.attributeNames()) {
                if (tagContext.evaluator().hasExpressions(element.attribute(name))) {
                    attributesDynamic = true;
                    break;
                }
            }
            bodyDynamic = tagContext.evaluator().hasExpressions(element.text());
        }
        dynamicityDetermined = true;
    }

    @Override
    public boolean isDynamic() {
        determineDynamicity();
        return bodyDynamic || attributesDynamic;
    }

    /** {@inheritDoc} The value is a string: the raw value, or the evaluated one. */
    @Override
    public Object getAttributeValue(String name) {
        if (!element.hasAttribute(name)) return "";
        String raw = element.attribute(name);
        determineDynamicity();
        return attributesDynamic ? tagContext.evaluator().evaluate(raw) : raw;
    }

    @Override
    public String getAttributeAsString(String name) {
        return String.valueOf(getAttributeValue(name));
    }

    @Override
    public boolean isTrue(String ofAttribute) {
        return Boolean.parseBoolean(getAttributeAsString(ofAttribute).strip());
    }

    // ─── the tag's declaration ──────────────────────────────────────────────────────────────────────────────────

    /** {@inheritDoc} {@code ""} if the class has no {@link MagidXmlTag}. */
    @Override
    public String getTargets() {
        MagidXmlTag declaration = getClass().getAnnotation(MagidXmlTag.class);
        return declaration == null ? "" : declaration.target();
    }

    /** {@inheritDoc} {@code false} for every type if the class has no {@link MagidXmlTag}. */
    @Override
    public boolean hasProcessingType(SemanticType type) {
        MagidXmlTag declaration = getClass().getAnnotation(MagidXmlTag.class);
        return declaration != null && Arrays.asList(declaration.types()).contains(type);
    }

    // ─── the story ──────────────────────────────────────────────────────────────────────────────────────────────

    @Override
    public StoryObjectModel som() {
        return tagContext.som();
    }

    @Override
    public void narrate(String text) {
        som().narrate(text);
    }

    @Override
    public void narrate(Map<String, ? extends BasicTypedValue<?>> fields) {
        som().narrate(fields);
    }

    @Override
    public void narrate(TagProcessorResultInterface result) {
        for (Map<String, String> fields : result.getResults()) {
            Map<String, NarrationValue<String>> line = new LinkedHashMap<>();
            fields.forEach((key, value) -> line.put(key, NarrationValue.of(value)));
            narrate(line);
        }
    }
}
