package com.zarterstein.magid.api.tags;

import java.util.Objects;

/**
 * What the story does after a tag has run: go on, leave the current block, or move the reader elsewhere. A tag
 * returns it in its {@link TagResult}; it never moves the reader itself, because the engine must first stop the
 * running action (a jump made from inside a tag would run the target scene in the middle of the current action).
 * <p>
 * Each {@link Type} is what a core tag returns, so a pack's outcome is handled exactly like the core tag's:
 * </p>
 * <table>
 *   <caption>Outcomes and their core tags</caption>
 *   <tr><th>Factory</th><th>Like</th><th>{@link #sceneId()}</th><th>{@link #addition()}</th></tr>
 *   <tr><td>{@link #proceed()}</td><td>most tags</td><td>—</td><td>—</td></tr>
 *   <tr><td>{@link #doBreak()}</td><td>{@code <break>}</td><td>—</td><td>—</td></tr>
 *   <tr><td>{@link #jump(String)}</td><td>{@code <jump scene="…">}</td><td>the scene</td><td>—</td></tr>
 *   <tr><td>{@link #act(String, String)}</td><td>{@code <jump>} to a choice</td><td>the scene</td><td>the choice's
 *       trigger</td></tr>
 *   <tr><td>{@link #stay()}</td><td>{@code <stay>}</td><td>—</td><td>—</td></tr>
 *   <tr><td>{@link #stayWithoutOnEntry()}</td><td>{@code <stay no-entry-action="true">}</td><td>—</td><td>—</td></tr>
 *   <tr><td>{@link #end(String)}, {@link #win(String)}, {@link #die(String)}</td><td>{@code <end>}, {@code <win>},
 *       {@code <die>}</td><td>—</td><td>the ending's name</td></tr>
 * </table>
 * The set of types is closed: the engine must understand every outcome, so packs can't add their own.
 *
 * @param type     what happens
 * @param sceneId  the target scene of {@link Type#JUMP} and {@link Type#ACT}; {@code null} otherwise
 * @param addition the choice's trigger for {@link Type#ACT}, the ending's name for {@link Type#END},
 *                 {@link Type#WIN} and {@link Type#DIE} ({@code ""} for the generic ending); {@code null} otherwise
 * @author Mark-Marion Gondelman (Creator)
 * @author Refactoring: Claude Code (agentic refactoring)
 * @author JavaDocs: Claude Code (Model: claude-opus-5-5)
 */
public record Outcome(Type type, String sceneId, String addition) {

    /** What happens after the tag has run. */
    public enum Type {
        /** Go on with the next tag. */
        PROCEED,
        /** Leave the current block or loop and go on after it. */
        BREAK,
        /** Go to a scene; its on-entry runs. */
        JUMP,
        /** Run a choice of a scene, as if the reader had chosen it; the scene's on-entry doesn't run. */
        ACT,
        /** Enter the current scene again; its on-entry runs again. */
        STAY,
        /** Enter the current scene again without running its on-entry. */
        STAY_WITHOUT_ON_ENTRY,
        /** End the story (a neutral ending). */
        END,
        /** End the story with a victory. */
        WIN,
        /** End the story with the reader's death. */
        DIE
    }

    /**
     * Creates an outcome; the factories are easier to read.
     *
     * @throws NullPointerException     if {@code type} is {@code null}
     * @throws IllegalArgumentException if {@code sceneId} or {@code addition} don't fit the type (see the table)
     */
    public Outcome {
        Objects.requireNonNull(type, "type");
        switch (type) {
            case JUMP -> {
                require(present(sceneId), type, "a scene");
                require(addition == null, type, "no addition");
            }
            case ACT -> {
                require(present(sceneId), type, "a scene");
                require(present(addition), type, "a choice's trigger");
            }
            case END, WIN, DIE -> {
                require(sceneId == null, type, "no scene");
                if ( addition == null ) addition = "";
            }
            case PROCEED, BREAK, STAY, STAY_WITHOUT_ON_ENTRY -> {
                require(sceneId == null && addition == null, type, "neither a scene nor an addition");
            }
        }
    }

    private static boolean present(String value) {
        return value != null && !value.isBlank();
    }

    private static void require(boolean condition, Type type, String what) {
        if ( !condition ) throw new IllegalArgumentException(type + " takes " + what);
    }

    /** @return go on with the next tag */
    public static Outcome proceed() { return new Outcome(Type.PROCEED, null, null); }

    /** @return leave the current block or loop, like {@code <break>} */
    public static Outcome doBreak() { return new Outcome(Type.BREAK, null, null); }

    /**
     * @param sceneId the scene to go to
     * @return go to that scene, like {@code <jump scene="…">}
     */
    public static Outcome jump(String sceneId) { return new Outcome(Type.JUMP, sceneId, null); }

    /**
     * @param sceneId the scene
     * @param choice  the trigger of one of its choices
     * @return run that choice, as if the reader had chosen it
     */
    public static Outcome act(String sceneId, String choice) { return new Outcome(Type.ACT, sceneId, choice); }

    /** @return enter the current scene again, running its on-entry, like {@code <stay>} */
    public static Outcome stay() { return new Outcome(Type.STAY, null, null); }

    /** @return enter the current scene again without its on-entry, like {@code <stay no-entry-action="true">} */
    public static Outcome stayWithoutOnEntry() { return new Outcome(Type.STAY_WITHOUT_ON_ENTRY, null, null); }

    /**
     * @param ending the ending's name; {@code null} or {@code ""} for the story's generic ending
     * @return end the story, like {@code <end>}
     */
    public static Outcome end(String ending) { return new Outcome(Type.END, null, ending); }

    /**
     * @param ending the ending's name; {@code null} or {@code ""} for the story's generic victory
     * @return end the story with a victory, like {@code <win>}
     */
    public static Outcome win(String ending) { return new Outcome(Type.WIN, null, ending); }

    /**
     * @param ending the ending's name; {@code null} or {@code ""} for the story's generic death
     * @return end the story with the reader's death, like {@code <die>}
     */
    public static Outcome die(String ending) { return new Outcome(Type.DIE, null, ending); }
}
