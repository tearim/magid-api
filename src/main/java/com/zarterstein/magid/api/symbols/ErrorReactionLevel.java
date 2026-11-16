package com.zarterstein.magid.api.symbols;

/**
 * How the engine reacts when a story breaks one of its symbol checks (a symbol used without being declared, a value
 * of another type written to a typed symbol). A story picks the level per check in its configuration.
 *
 * @author Mark-Marion Gondelman (Creator)
 * @author JavaDocs: Claude Code (Model: claude-opus-5-5)
 */
public enum ErrorReactionLevel {
    /** Ignore it. */
    NONE,
    /** Log it at info level and go on. */
    LOG,
    /** Log it as a warning and go on. */
    WARNING,
    /** Throw an exception. */
    THROW
}
