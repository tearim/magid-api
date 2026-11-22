package com.zarterstein.magid.api.access;

/**
 * Gives each tagpack its own {@link Caller} for one story session. The server builds one from the session's account
 * and hands it to the engine; the engine asks it with the pack's id whenever a tag of that pack wants its caller.
 *
 * @author Mark-Marion Gondelman (Design, Orchestration)
 * @author Code: Claude Code (agentic implementation)
 */
@FunctionalInterface
public interface CallerSource {

    /** A source without an account: every pack gets {@link Caller#NONE}. */
    CallerSource NONE = packId -> Caller.NONE;

    /**
     * @param packId the pack's id (its manifest's id); {@link com.zarterstein.magid.api.packs.PackManifest#TAGPACK_CORE}
     *               for the engine's own tags
     * @return the caller as that pack may see it; never {@code null}
     */
    Caller forPack(String packId);
}
