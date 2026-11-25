package com.zarterstein.magid.api.packs;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link PluginResources}: which keys are valid, what {@link PluginResources#NONE} finds, and how {@code text} and
 * {@code exists} read {@code bytes}.
 *
 * @author Mark-Marion Gondelman (Orchestration)
 * @author Code: Claude Code (agentic implementation)
 */
class PluginResourcesTest {

    // ─── valid keys ─────────────────────────────────────────────────────────

    @Test
    void aFileNameIsAKey() {
        assertTrue(PluginResources.isValidKey("decide.default.txt"));
    }

    @Test
    void aPathOfPartsIsAKey() {
        assertTrue(PluginResources.isValidKey("prompts/decide.default.txt"));
    }

    @Test
    void anEncodedModelNameIsAKey() {
        assertTrue(PluginResources.isValidKey("prompts/decide.qwen2.5-3b__latest.txt"));
    }

    @Test
    void aKeyOfTheLongestLengthIsAKey() {
        assertTrue(PluginResources.isValidKey("a".repeat(PluginResources.MAX_KEY_LENGTH)));
    }

    // ─── invalid keys ───────────────────────────────────────────────────────

    @Test
    void nullIsNoKey() {
        assertFalse(PluginResources.isValidKey(null));
    }

    @Test
    void anEmptyTextIsNoKey() {
        assertFalse(PluginResources.isValidKey(""));
    }

    @Test
    void climbingOutIsNoKey() {
        assertFalse(PluginResources.isValidKey("../secrets.txt"));
    }

    @Test
    void climbingOutInTheMiddleIsNoKey() {
        assertFalse(PluginResources.isValidKey("prompts/../../secrets.txt"));
    }

    @Test
    void theCurrentDirectoryIsNoPart() {
        assertFalse(PluginResources.isValidKey("prompts/./decide.txt"));
    }

    @Test
    void anAbsolutePathIsNoKey() {
        assertFalse(PluginResources.isValidKey("/etc/passwd"));
    }

    @Test
    void aBackslashIsNoKey() {
        assertFalse(PluginResources.isValidKey("prompts\\decide.txt"));
    }

    @Test
    void aWindowsDriveIsNoKey() {
        assertFalse(PluginResources.isValidKey("C:/secrets.txt"));
    }

    @Test
    void aDoubleSlashIsNoKey() {
        assertFalse(PluginResources.isValidKey("prompts//decide.txt"));
    }

    @Test
    void aTrailingSlashIsNoKey() {
        assertFalse(PluginResources.isValidKey("prompts/"));
    }

    @Test
    void aHiddenFileIsNoKey() {
        assertFalse(PluginResources.isValidKey(".env"));
    }

    @Test
    void aColonIsNoKey() {
        assertFalse(PluginResources.isValidKey("decide.qwen2.5:latest.txt"));
    }

    @Test
    void aSpaceIsNoKey() {
        assertFalse(PluginResources.isValidKey("my prompt.txt"));
    }

    @Test
    void aKeyLongerThanTheLimitIsNoKey() {
        assertFalse(PluginResources.isValidKey("a".repeat(PluginResources.MAX_KEY_LENGTH + 1)));
    }

    // ─── checkKey ───────────────────────────────────────────────────────────

    @Test
    void checkKeyReturnsAValidKey() {
        assertEquals("prompts/a.txt", PluginResources.checkKey("prompts/a.txt"));
    }

    @Test
    void checkKeyRefusesAnInvalidKey() {
        assertThrows(IllegalArgumentException.class, () -> PluginResources.checkKey("../a.txt"));
    }

    @Test
    void checkKeyNamesTheKeyItRefuses() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> PluginResources.checkKey("../a.txt"));

        assertTrue(e.getMessage().contains("`../a.txt`"), e.getMessage());
    }

    // ─── NONE ───────────────────────────────────────────────────────────────

    @Test
    void noneFindsNoBytes() {
        assertEquals(Optional.empty(), PluginResources.NONE.bytes("prompts/a.txt"));
    }

    @Test
    void noneFindsNoText() {
        assertEquals(Optional.empty(), PluginResources.NONE.text("prompts/a.txt"));
    }

    @Test
    void noneSearchesNowhere() {
        assertEquals(List.of(), PluginResources.NONE.searchPath("prompts/a.txt"));
    }

    @Test
    void noneRefusesAnInvalidKeyForBytes() {
        assertThrows(IllegalArgumentException.class, () -> PluginResources.NONE.bytes("../a.txt"));
    }

    @Test
    void noneRefusesAnInvalidKeyForText() {
        assertThrows(IllegalArgumentException.class, () -> PluginResources.NONE.text("../a.txt"));
    }

    @Test
    void noneHasNoFileThere() {
        assertFalse(PluginResources.NONE.exists("prompts/a.txt"));
    }

    @Test
    void noneRefusesAnInvalidKeyForExists() {
        assertThrows(IllegalArgumentException.class, () -> PluginResources.NONE.exists("../a.txt"));
    }

    @Test
    void noneRefusesAnInvalidKeyForTheSearchPath() {
        assertThrows(IllegalArgumentException.class, () -> PluginResources.NONE.searchPath("../a.txt"));
    }

    // ─── text over bytes ────────────────────────────────────────────────────

    /** Files in a map, as an engine would find them. */
    private static PluginResources filesOf(Map<String, byte[]> files) {
        return new PluginResources() {
            @Override
            public Optional<byte[]> bytes(String key) {
                return Optional.ofNullable(files.get(PluginResources.checkKey(key)));
            }

            @Override
            public List<String> searchPath(String key) {
                return List.of("classpath:/" + PluginResources.checkKey(key));
            }
        };
    }

    @Test
    void textIsReadAsUtf8() {
        PluginResources files = filesOf(Map.of("a.txt", "Ælfwine’s «prompt»".getBytes(StandardCharsets.UTF_8)));

        assertEquals(Optional.of("Ælfwine’s «prompt»"), files.text("a.txt"));
    }

    @Test
    void anEmptyFileIsAnEmptyText() {
        PluginResources files = filesOf(Map.of("a.txt", new byte[0]));

        assertEquals(Optional.of(""), files.text("a.txt"));
    }

    @Test
    void aMissingFileIsNoText() {
        PluginResources files = filesOf(Map.of());

        assertEquals(Optional.empty(), files.text("a.txt"));
    }

    @Test
    void aFileThereExists() {
        PluginResources files = filesOf(Map.of("a.txt", "a".getBytes(StandardCharsets.UTF_8)));

        assertTrue(files.exists("a.txt"));
    }

    @Test
    void anEmptyFileExists() {
        PluginResources files = filesOf(Map.of("a.txt", new byte[0]));

        assertTrue(files.exists("a.txt"));
    }

    @Test
    void aMissingFileDoesNotExist() {
        assertFalse(filesOf(Map.of()).exists("a.txt"));
    }

    @Test
    void existsRefusesAnInvalidKey() {
        assertThrows(IllegalArgumentException.class, () -> filesOf(Map.of()).exists("../a.txt"));
    }

    @Test
    void aFallbackChainTakesTheFirstFileThere() {
        PluginResources files = filesOf(Map.of("prompts/decide.default.txt", "default".getBytes(StandardCharsets.UTF_8)));

        String prompt = files.text("prompts/decide.qwen2.5-3b__latest.txt")
                .or(() -> files.text("prompts/decide.default.txt"))
                .orElseThrow();

        assertEquals("default", prompt);
    }
}
