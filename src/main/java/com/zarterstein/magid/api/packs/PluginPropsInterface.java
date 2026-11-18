package com.zarterstein.magid.api.packs;

/**
 * A plugin's own server settings, read by key without its prefix: the engine looks {@code endpoint} up as
 * {@code -Dmagid.plugin.<id>.endpoint}, then the environment variable {@code MAGID_PLUGIN_<ID>_ENDPOINT}, then
 * {@code endpoint=} in {@code <plugins>/<id>.properties}. A tag reaches its pack's settings through
 * {@link com.zarterstein.magid.api.tags.TagContext#pluginProps()}, never another pack's.
 * <p>
 * Never copy a secret from here into a story symbol: symbols are saved, shown and shared.
 * </p>
 *
 * @author Mark-Marion Gondelman (Creator)
 * @author JavaDocs: Claude Code (Model: claude-opus-5-5)
 */
public interface PluginPropsInterface {

    /** Settings with no keys: every lookup gives its fallback. For contexts without settings, e.g. a test's fake. */
    PluginPropsInterface NONE = (key, defaultValue) -> defaultValue;

    /**
     * @param key          the key, without the {@code magid.plugin.<id>.} prefix
     * @param defaultValue the value when no source sets the key
     * @return the setting, or {@code defaultValue}
     */
    String get(String key, String defaultValue);

    /**
     * @param key the key, without the {@code magid.plugin.<id>.} prefix
     * @return the setting, or {@code ""} when no source sets it
     */
    default String get(String key) {
        return get(key, "");
    }
}
