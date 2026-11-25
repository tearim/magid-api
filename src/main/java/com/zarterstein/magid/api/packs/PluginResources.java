package com.zarterstein.magid.api.packs;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Files a plugin reads by key, wherever it runs: from an IDE's class path, a dev server, a jar or a directory in the
 * plugin directory, Docker. A key such as {@code prompts/decide.default.txt} is looked up in two places; the first
 * that has it wins:
 * <ol>
 *   <li>the server's: {@code <plugins>/<plugin-id>/<key>}, where whoever runs the server adds or replaces files;</li>
 *   <li>the plugin's own: {@code <key>} on the plugin's class path (its {@code src/main/resources}), the defaults it
 *   ships.</li>
 * </ol>
 * A tag reaches its own pack's files through {@link com.zarterstein.magid.api.tags.TagContext#pluginResources()},
 * never another pack's. There is no call for "the resource directory": a jar has none (the engine's
 * TAGPACKS-DESIGN §10.4).
 * <p>
 * A key is one or more parts joined by {@code /}; each part starts with a letter or digit and holds only letters,
 * digits, {@code .}, {@code _} and {@code -} ({@link #isValidKey}). So a key can't climb out ({@code ..}), start at
 * the root ({@code /etc}) or use {@code \}. A key built from a story's text (a model's name) must be encoded first:
 * an invalid key is the plugin's bug and throws {@link IllegalArgumentException}.
 * </p>
 * <p>
 * Never ship a secret as a resource: a jar is readable by whoever has it.
 * </p>
 *
 * @author Mark-Marion Gondelman (Creator)
 * @author JavaDocs: Claude Code (Model: claude-opus-5-5)
 * @author Refactoring: Claude Code (agentic refactoring)
 */
public interface PluginResources {

    /** The largest file read, in bytes (1 MiB); a larger one counts as missing, with a warning in the log. */
    int MAX_BYTES = 1 << 20;

    /** The longest key, in characters. */
    int MAX_KEY_LENGTH = 255;

    /** One part of a key: a letter or digit, then letters, digits, {@code .}, {@code _}, {@code -}. */
    Pattern KEY_PART = Pattern.compile("[A-Za-z0-9][A-Za-z0-9._-]*");

    /**
     * No files: every lookup is empty and nothing is searched. For contexts without an engine, e.g. a test's fake.
     * Keys are still checked, so a fake catches an invalid key as the engine would.
     */
    PluginResources NONE = new PluginResources() {
        @Override
        public Optional<byte[]> bytes(String key) {
            checkKey(key);
            return Optional.empty();
        }

        @Override
        public List<String> searchPath(String key) {
            checkKey(key);
            return List.of();
        }
    };

    /**
     * @param key the file's key, e.g. {@code img/seal.png}
     * @return the file's bytes, from the first place that has it; empty if none has it, or if it is larger than
     *         {@link #MAX_BYTES}
     * @throws IllegalArgumentException if the key is not valid ({@link #isValidKey})
     */
    Optional<byte[]> bytes(String key);

    /**
     * @param key the file's key, e.g. {@code prompts/decide.default.txt}
     * @return the file's text, read as UTF-8, from the first place that has it; empty if none has it. An empty file
     *         is {@code Optional.of("")}: "not there" and "there, but empty" stay apart.
     * @throws IllegalArgumentException if the key is not valid ({@link #isValidKey})
     */
    default Optional<String> text(String key) {
        return bytes(key).map(b -> new String(b, StandardCharsets.UTF_8));
    }

    /**
     * Whether a file is there, as {@link #bytes} would find it: a file larger than {@link #MAX_BYTES} is not. For
     * choosing among keys ({@code decide.qwen2.5-3b__latest.txt}, then {@code decide.default.txt}) and for checking a
     * pack's files while developing it. An engine may answer without copying the bytes.
     *
     * @param key the file's key
     * @return {@code true} if {@link #bytes} would return the file
     * @throws IllegalArgumentException if the key is not valid ({@link #isValidKey})
     */
    default boolean exists(String key) {
        return bytes(key).isPresent();
    }

    /**
     * Where {@link #bytes} looks for a key, in order, for messages: an absolute path in the server's directory (left
     * out if the server has no plugin directory), then {@code classpath:/<key>}. Tells whoever runs the server where
     * a missing file goes.
     *
     * @param key the file's key
     * @return the places, in the order they are searched; empty if nothing is searched ({@link #NONE})
     * @throws IllegalArgumentException if the key is not valid ({@link #isValidKey})
     */
    List<String> searchPath(String key);

    /**
     * @param key a key, possibly {@code null}
     * @return whether it is a valid key: at most {@link #MAX_KEY_LENGTH} characters, parts joined by single
     *         {@code /}, each part matching {@link #KEY_PART}
     */
    static boolean isValidKey(String key) {
        if (key == null || key.isEmpty() || key.length() > MAX_KEY_LENGTH) return false;
        for (String part : key.split("/", -1)) {
            if (!KEY_PART.matcher(part).matches()) return false;
        }
        return true;
    }

    /**
     * @param key a key
     * @return the key, if it is valid
     * @throws IllegalArgumentException if it is not ({@link #isValidKey}), naming it
     */
    static String checkKey(String key) {
        if (!isValidKey(key)) {
            throw new IllegalArgumentException("Not a resource key: `" + key + "`. A key is parts joined by `/`, each "
                    + "a letter or digit followed by letters, digits, `.`, `_` or `-`; at most " + MAX_KEY_LENGTH
                    + " characters.");
        }
        return key;
    }
}
