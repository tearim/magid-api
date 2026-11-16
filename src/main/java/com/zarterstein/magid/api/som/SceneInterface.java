package com.zarterstein.magid.api.som;

import java.util.List;
import java.util.Optional;

/**
 * A scene of the story as a tag sees it: its descriptions and its choices. A handle, not a copy: it reads the scene
 * on every call, and its changes go through the engine, so they are saved with the session (see
 * {@link StoryObjectModel}).
 *
 * @author Mark-Marion Gondelman (Creator)
 * @author JavaDocs: Claude Code (Model: claude-opus-5-5)
 */
public interface SceneInterface {

    /** @return the scene's id */
    String getId();

    /** @return the scene's description, the text the reader sees on entering it */
    String getDescription();

    /**
     * Replaces the scene's description, as {@code <set-description>} does.
     *
     * @param text the new description
     */
    void setDescription(String text);

    /** @return the scene's short description */
    String getShortDescription();

    /**
     * Replaces the scene's short description.
     *
     * @param text the new short description
     */
    void setShortDescription(String text);

    /** @return the scene's choices, hidden ones included */
    List<ChoiceInterface> choices();

    /**
     * @param trigger a choice's trigger
     * @return the choice, or empty if the scene has no choice with that trigger
     */
    Optional<ChoiceInterface> choice(String trigger);

    /**
     * Adds a choice the reader is offered, as {@code <add-choice>} does.
     *
     * @param trigger  what the reader chooses (the choice's command)
     * @param actionId the id of the action it runs
     * @return the new choice
     */
    ChoiceInterface addChoice(String trigger, String actionId);

    /**
     * Adds a hidden choice: it isn't offered to the reader, but it can be run (e.g. by a jump to
     * {@code trigger@scene}) or shown later.
     *
     * @param trigger  the choice's trigger
     * @param actionId the id of the action it runs
     * @return the new choice
     */
    ChoiceInterface addHiddenChoice(String trigger, String actionId);
}
