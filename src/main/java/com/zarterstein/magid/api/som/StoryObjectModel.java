package com.zarterstein.magid.api.som;

import com.zarterstein.magid.api.values.BasicTypedValue;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The Story Object Model (SOM) of one session: the story's runtime structure (scenes, their descriptions and
 * choices, fragments) as a tag sees it. It is not the parsed MagidML, which tags only read; it is what the reader
 * plays, and tags may change it as core tags do ({@code <add-choice>}, {@code <hide-choice>},
 * {@code <set-description>}).
 * <p>
 * Every change made through the SOM's handles goes through the engine's own methods, so it is recorded like a core
 * tag's change and survives saving and restoring the session. Each session has its own SOM: a change affects that
 * reader only. The SOM and its handles are valid while the tag runs; don't keep them for later.
 * </p>
 * <p>
 * Jumping to another scene is not a SOM change: a tag returns it as its outcome, as {@code <jump>} does.
 * </p>
 *
 * @author Mark-Marion Gondelman (Creator)
 * @author JavaDocs: Claude Code (Model: claude-opus-5-5)
 */
public interface StoryObjectModel {

    /** @return the id of the scene the reader is in */
    String currentSceneId();

    /** @return the ids of all scenes of the story, endings included */
    List<String> sceneIds();

    /**
     * @param sceneId a scene's id
     * @return the scene, or empty if the story has no scene with that id
     */
    Optional<SceneInterface> scene(String sceneId);

    /**
     * @param name a fragment's name
     * @return whether the story (or a tag, during play) defined a fragment with that name
     */
    boolean hasFragment(String name);

    /**
     * Narrates a line of plain text, without fields. Symbols in braces ({@code {player.gold}}) are substituted.
     *
     * @param text the text
     */
    void narrate(String text);

    /**
     * Narrates a line made of fields, which the engine sends to the client as one JSON object. {@code text} holds
     * the line's text; other keys are what the clients understand, e.g. {@code text-class} and the styling keys.
     * Symbols in braces are substituted in text values only.
     *
     * @param narration the fields: names and values ({@link NarrationValue}, or any other
     *                  {@link BasicTypedValue}, e.g. a recalled reverie)
     */
    void narrate(Map<String, ? extends BasicTypedValue<?>> narration);
}
