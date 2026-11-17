package com.zarterstein.magid.api.packs;

/**
 * The manifest attributes of a Magid plugin, and the values a tagpack gives them:
 * <pre>
 * Magid-Plugin-Type: tag-pack
 * Magid-Plugin-Id: ollama
 * Magid-Plugin-Version: 1.2.0
 * Magid-Requires-Api: [0.1,0.2)
 * Magid-Tag-Prefix: llm
 * </pre>
 * {@link #TYPE}, {@link #ID} and {@link #VERSION} apply to every plugin type; the others to tagpacks.
 *
 * @author Mark-Marion Gondelman (Orchestration)
 * @author Code: Claude Code (agentic implementation)
 */
public final class PackManifest {

    /** What kind of plugin it is: lower-case words joined by hyphens. */
    public static final String TYPE = "Magid-Plugin-Type";
    /** The plugin's id: lower-case words joined by hyphens, unique in a plugin directory. */
    public static final String ID = "Magid-Plugin-Id";
    /** The plugin's version, a semantic version such as {@code 1.2.0}. */
    public static final String VERSION = "Magid-Plugin-Version";
    /** The {@code magid-api} versions a tagpack works with, a Maven range such as {@code [0.1,0.2)}. */
    public static final String REQUIRES_API = "Magid-Requires-Api";
    /**
     * A tagpack's default prefix: lower-case letters and digits, starting with a letter, without a hyphen, not
     * {@code none}.
     */
    public static final String TAG_PREFIX = "Magid-Tag-Prefix";

    /** The {@link #TYPE} of a tagpack. */
    public static final String TAG_PACK_TYPE = "tag-pack";

    private PackManifest() {
    }
}
