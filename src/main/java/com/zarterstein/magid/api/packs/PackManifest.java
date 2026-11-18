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
    /**
     * The plugin's id: lower-case letters and digits, words joined by single hyphens, no underscores
     * ({@link #ID_FORM}); unique in a plugin directory.
     */
    public static final String ID = "Magid-Plugin-Id";
    /** The plugin's version, a semantic version such as {@code 1.2.0}. */
    public static final String VERSION = "Magid-Plugin-Version";
    /** The {@code magid-api} versions a tagpack works with, a Maven range such as {@code [0.1,0.2)}. */
    public static final String REQUIRES_API = "Magid-Requires-Api";
    /**
     * A tagpack's default prefix: lower-case letters and digits, starting with a letter, without a hyphen or an
     * underscore, not {@code none} ({@link #PREFIX_FORM}, {@link #NO_PREFIX}).
     */
    public static final String TAG_PREFIX = "Magid-Tag-Prefix";

    /** The {@link #TYPE} of a tagpack. */
    public static final String TAG_PACK_TYPE = "tag-pack";
    /**
     * The one word a {@link #TAG_PREFIX} may not be: a story writes {@code <extend with="…" as="none"/>} to use a
     * pack's tags without a prefix.
     */
    public static final String NO_PREFIX = "none";

    /**
     * The form of an {@link #ID}. No underscore: a plugin's settings {@code magid.plugin.<id>.<key>} are also read
     * from environment variables, where {@code .} becomes {@code _}, so an underscore in an id would make two
     * settings share one variable.
     */
    public static final String ID_FORM = "[a-z0-9]+(-[a-z0-9]+)*";
    /**
     * The form of a {@link #TAG_PREFIX}: a letter, then letters and digits. It starts with a letter because it starts
     * an XML name ({@code <llm-response>}); it has no hyphen because the hyphen ends it.
     */
    public static final String PREFIX_FORM = "[a-z][a-z0-9]*";

    /**
     * @param id a plugin id, or {@code null}
     * @return whether {@code id} has the {@link #ID_FORM}; {@code false} for {@code null}
     */
    public static boolean isValidId(String id) {
        return id != null && id.matches(ID_FORM);
    }

    /**
     * @param p a tag prefix, or {@code null}
     * @return whether {@code p} has the {@link #PREFIX_FORM} and isn't {@link #NO_PREFIX}; {@code false} for
     *         {@code null}
     */
    public static boolean isValidTagPrefix(String p) {
        return p != null && p.matches(PREFIX_FORM) && !p.equals(NO_PREFIX);
    }

    private PackManifest() {
    }
}
