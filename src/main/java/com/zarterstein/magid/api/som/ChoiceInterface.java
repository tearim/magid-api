package com.zarterstein.magid.api.som;

/**
 * A choice of a scene as a tag sees it: what the reader can choose there and the action it runs. A handle, not a
 * copy: it is identified by its scene and its trigger, and its changes go through the engine, so they are saved with
 * the session (see {@link StoryObjectModel}).
 *
 * @author Mark-Marion Gondelman (Creator)
 * @author JavaDocs: Claude Code (Model: claude-opus-5-5)
 */
public interface ChoiceInterface {

    /** @return what the reader chooses (the choice's command); unique within its scene */
    String getTrigger();

    /** @return the id of the action the choice runs */
    String getActionId();

    /** @return whether the choice is hidden: not offered to the reader, though it can still be run */
    boolean isHidden();

    /** Hides the choice, as {@code <hide-choice>} does. */
    void hide();

    /** Offers a hidden choice to the reader again. */
    void show();

    /** Removes the choice from its scene. This handle is no longer usable afterwards. */
    void remove();
}
