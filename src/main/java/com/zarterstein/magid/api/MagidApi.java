package com.zarterstein.magid.api;

/**
 * Facts about this {@code magid-api} itself.
 *
 * @author Mark-Marion Gondelman (Orchestration)
 * @author Code: Claude Code (agentic implementation)
 */
public final class MagidApi {

    /** The package every API type lives under, with a trailing dot. */
    public static final String PACKAGE = "com.zarterstein.magid.api.";

    /** Keep in step with the pom's {@code <version>}; {@code MagidApiTest} fails when they differ. */
    private static final String VERSION = "0.1-SNAPSHOT";

    private MagidApi() {
    }

    /**
     * The version of the {@code magid-api} on the class path at run time: the one a server checks a pack's
     * {@code Magid-Requires-Api} against. A method rather than a constant, so the compiler doesn't copy the value into
     * the classes that read it: they always get the version they run with, not the one they were built against.
     *
     * @return e.g. {@code 0.1-SNAPSHOT}
     */
    public static String version() {
        return VERSION;
    }
}
