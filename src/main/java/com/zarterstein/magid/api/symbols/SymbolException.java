package com.zarterstein.magid.api.symbols;

/**
 * A symbol can't be written: its name is malformed (an empty key or owner, e.g. {@code player.}), it is a constant
 * that is already defined, or it breaks a check whose level is {@link ErrorReactionLevel#THROW}. Reading never throws
 * it (see {@link SymbolPinax}).
 * <p>
 * Unchecked, like the engine's exceptions.
 * </p>
 *
 * @author Mark-Marion Gondelman (Creator)
 * @author Refactoring: Claude Code (agentic refactoring)
 * @author JavaDocs: Claude Code (Model: claude-opus-5-5)
 */
public class SymbolException extends RuntimeException {

    /**
     * @param message what is wrong, for the author
     */
    public SymbolException(String message) {
        super(message);
    }
}
