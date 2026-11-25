package com.zarterstein.magid.api.tags;

import com.zarterstein.magid.api.access.Caller;
import com.zarterstein.magid.api.expressions.EvaluatorInterface;
import com.zarterstein.magid.api.packs.PluginPropsInterface;
import com.zarterstein.magid.api.packs.PluginResources;
import com.zarterstein.magid.api.som.StoryObjectModel;
import com.zarterstein.magid.api.streams.TagStreams;
import com.zarterstein.magid.api.symbols.SymbolPinax;

import java.util.List;
import java.util.logging.Logger;

/**
 * What the engine offers a tag while it runs, besides its element: the story model, the symbols, the expressions,
 * and running or rendering blocks of MagidML. The engine binds it with {@link MagidTagInterface#bind}; a tag reaches
 * it through {@link MagidTag}'s helpers or the {@code context} field.
 *
 * @author Mark-Marion Gondelman (Orchestration)
 * @author Code: Claude Code (agentic implementation)
 */
public interface TagContext {

    /**
     * @return the story model: scenes, choices, fragments, narration
     */
    StoryObjectModel som();

    /**
     * @return the story's symbols (memories, attributes, variables, constants)
     */
    SymbolPinax symbols();

    /**
     * @return the story's expressions
     */
    EvaluatorInterface evaluator();

    /**
     * @return the block the tag stands in ({@link MagidTagInterface#blockName()}); {@code ""} if none
     */
    String blockName();

    /**
     * Runs nodes as an action runs them: tag by tag, until one says to leave the block.
     *
     * @param nodes the nodes, usually a tag's {@link TagElement#children()}
     * @return the outcome of the tag that left the block; {@link Outcome#proceed()} if all ran
     */
    Outcome executeBlock(List<TagNode> nodes);

    /**
     * Renders nodes as a narration block renders them: their text, with the tags' contributions.
     *
     * @param nodes the nodes, usually a tag's {@link TagElement#children()}
     * @return the rendered results
     */
    TagProcessorResultInterface renderBlock(List<TagNode> nodes);

    /**
     * @return a logger for the tag's pack
     */
    Logger logger();

    /**
     * The settings of the tag's own pack ({@link PluginPropsInterface}). The engine gives each tag its pack's
     * settings; this default, for contexts that have none (a test's fake), gives every key its fallback.
     *
     * @return the pack's settings; never {@code null}
     */
    default PluginPropsInterface pluginProps() {
        return PluginPropsInterface.NONE;
    }

    /**
     * @return the session's stash: objects tags keep for later, not saved, cleared when the story or the tagpacks are
     *         reloaded ({@link StoryObjectStash})
     */
    StoryObjectStash stash();

    /**
     * Who runs the tag, as the tag's own pack may see it ({@link Caller}): the access level, an id of its own for the
     * account, the locale, group checks. The engine gives each pack its own; this default, for contexts that have no
     * account (a test's fake), gives {@link Caller#NONE}.
     *
     * @return the caller; never {@code null}
     */
    default Caller caller() {
        return Caller.NONE;
    }

    /**
     * Streams: text the tag goes on producing after the response ({@link com.zarterstein.magid.api.streams.TagStream}).
     * This default, for contexts that can't stream (a test's fake, a client that doesn't take streams), opens only
     * {@link com.zarterstein.magid.api.streams.TagStream#NONE}.
     *
     * @return the session's streams; never {@code null}
     */
    default TagStreams streams() {
        return TagStreams.NONE;
    }

    /**
     * Files of the tag's own pack, read by key ({@link PluginResources}): the server's
     * {@code <plugins>/<pack-id>/<key>} first, then the pack's class path. The engine gives each tag its pack's; this
     * default, for contexts without an engine (a test's fake), finds nothing ({@link PluginResources#NONE}).
     *
     * @return the pack's files; never {@code null}
     */
    default PluginResources pluginResources() {
        return PluginResources.NONE;
    }

    /**
     * Whether the server runs in development mode ({@code magid.dev}; the engine's is
     * {@code MagidConfig.isEnabled("magid.dev")}), e.g. for debug output a tag gives only there.
     * This default, for contexts without an engine (a test's fake), is off.
     *
     * @return {@code true} on a dev server
     */
    default boolean isDevMode() {
        return false;
    }

    /**
     * Magid's one yes/no rule, as the engine applies it to its settings: {@code enabled}, {@code true}, {@code 1} and
     * {@code yes} (any case) are on; everything else, {@code null} included, is off. For a tag's own on/off
     * attributes, so they read as the engine's switches do. The engine's is {@code MagidConfig.testEnabled(String)}
     * (com.zarterstein.MagidConfig, magid-core), the rule's one source. Takes a <b>value</b>, not a setting's key:
     * {@code isEnabled(evaluated(el, "debug-info"))}. This default, for contexts without an engine, is off.
     *
     * @param value the value to test, e.g. an attribute's; may be {@code null}
     * @return {@code true} if the value means "on"
     */
    default boolean isEnabled(String value) {
        return false;
    }
}
