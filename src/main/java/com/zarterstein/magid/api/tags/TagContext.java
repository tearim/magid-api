package com.zarterstein.magid.api.tags;

import com.zarterstein.magid.api.expressions.EvaluatorInterface;
import com.zarterstein.magid.api.packs.PluginPropsInterface;
import com.zarterstein.magid.api.som.StoryObjectModel;
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
}
