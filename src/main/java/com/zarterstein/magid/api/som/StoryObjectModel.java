package com.zarterstein.magid.api.som;

import com.zarterstein.magid.api.tags.TagResult;
import com.zarterstein.magid.api.values.BasicTypedValue;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/*
 *          SOM, Story Object Model.   Сом (som), Russian for "catfish".
 *
 *                            ,
 *                           /|
 *               _.-----..../ |_.------.._
 *           _.-'   (o)                     `-._               ,--.
 *     ___.-'                                   `-.__       ,-'   /
 *    <___   ======                                  `-----<     (
 *        `-.__                                      _.---- `-.   \
 *       /  \  `--..___                  ___...--''          `--'
 *      /    \         ``---...___...--''
 *    _/      \_                                    ~  o
 *   /          \        it lies at the bottom   ~   O    ~
 *                       of every story,          o   ~
 *                       whiskers in the scenes.
 */

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
     * Narrates a line of plain text, without fields. When the engine's expressions are enabled
     * ({@code magid.expressions}), symbols in braces ({@code {player.gold}}) are substituted and expressions evaluated.
     *
     * @param text the text
     */
    void narrate(String text);

    /**
     * Narrates a line made of fields, which the engine sends to the client as one JSON object. {@code text} holds
     * the line's text; other keys are what the clients understand, e.g. {@code text-class} and the styling keys.
     * Substitution works as in {@link #narrate(String)}, in text values only.
     *
     * @param narration the fields: names and values ({@link NarrationValue}, or any other
     *                  {@link BasicTypedValue}, e.g. a recalled reverie)
     */
    void narrate(Map<String, ? extends BasicTypedValue<?>> narration);

    /**
     * Runs a fragment, as {@code <use-fragment>} does, and returns what it did. A fragment that jumps, stays, breaks
     * or ends the story doesn't do so itself: its outcome comes back in the result, and the tag decides whether to
     * return it as its own (as {@code <use-fragment>} does) or to ignore it.
     * <pre>{@code
     * TagResult done = som.executeFragment("search-room");
     * return done;                                   // a jump inside the fragment jumps
     * }</pre>
     *
     * @param name the fragment's name
     * @return the fragment's outcome; for a break or a stay, its text is the message the fragment gave
     * @throws IllegalArgumentException if the story has no fragment with that name (see {@link #hasFragment(String)})
     */
    TagResult executeFragment(String name);
}
