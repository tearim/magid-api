package com.zarterstein.magid.api.packs;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link PackManifest}: the forms of a plugin id and of a tagpack's prefix.
 *
 * @author Mark-Marion Gondelman (Orchestration)
 * @author Code: Claude Code (agentic implementation)
 */
class PackManifestTest {

    @Nested
    @DisplayName("isValidId: lower-case words of letters and digits joined by single hyphens")
    class Ids {

        @Test
        @DisplayName("One word is an id")
        void oneWord() {
            assertTrue(PackManifest.isValidId("ollama"));
        }

        @Test
        @DisplayName("Words joined by hyphens are an id")
        void hyphenatedWords() {
            assertTrue(PackManifest.isValidId("magid-math"));
        }

        @Test
        @DisplayName("Digits are allowed, also first")
        void digits() {
            assertTrue(PackManifest.isValidId("3d-dice"));
        }

        @Test
        @DisplayName("An underscore is not allowed: it would clash in environment variables (MAGID_PLUGIN_A_B_KEY)")
        void underscore() {
            assertFalse(PackManifest.isValidId("a_b"));
        }

        @Test
        @DisplayName("An underscore is not allowed after a hyphen either")
        void underscoreAfterHyphen() {
            assertFalse(PackManifest.isValidId("a-b_c"));
        }

        @Test
        @DisplayName("Upper case is not allowed")
        void upperCase() {
            assertFalse(PackManifest.isValidId("Ollama"));
        }

        @Test
        @DisplayName("A leading, trailing or double hyphen is not allowed")
        void strayHyphens() {
            assertFalse(PackManifest.isValidId("-ollama"));
            assertFalse(PackManifest.isValidId("ollama-"));
            assertFalse(PackManifest.isValidId("magid--math"));
        }

        @Test
        @DisplayName("A path is not an id")
        void path() {
            assertFalse(PackManifest.isValidId("../ollama"));
            assertFalse(PackManifest.isValidId("a/b"));
        }

        @Test
        @DisplayName("The engine's own pack id is reserved")
        void core() {
            assertFalse(PackManifest.isValidId(PackManifest.TAGPACK_CORE));
        }

        @Test
        @DisplayName("An id that only starts with the engine's pack id is fine")
        void startsWithCore() {
            assertTrue(PackManifest.isValidId("core-extras"));
        }

        @Test
        @DisplayName("Empty and null are not ids")
        void emptyAndNull() {
            assertFalse(PackManifest.isValidId(""));
            assertFalse(PackManifest.isValidId(null));
        }
    }

    @Nested
    @DisplayName("isValidTagPrefix: lower-case letters and digits, starting with a letter, not 'none'")
    class Prefixes {

        @Test
        @DisplayName("Letters are a prefix")
        void letters() {
            assertTrue(PackManifest.isValidTagPrefix("llm"));
        }

        @Test
        @DisplayName("Digits after the first letter are allowed")
        void digitsAfterLetter() {
            assertTrue(PackManifest.isValidTagPrefix("m3"));
        }

        @Test
        @DisplayName("A digit first is not allowed: <9x-calc> is not an XML name")
        void digitFirst() {
            assertFalse(PackManifest.isValidTagPrefix("9x"));
        }

        @Test
        @DisplayName("An underscore is not allowed")
        void underscore() {
            assertFalse(PackManifest.isValidTagPrefix("my_pack"));
        }

        @Test
        @DisplayName("A hyphen is not allowed: it separates the prefix from the tag's local name")
        void hyphen() {
            assertFalse(PackManifest.isValidTagPrefix("ol-lm"));
        }

        @Test
        @DisplayName("Upper case is not allowed")
        void upperCase() {
            assertFalse(PackManifest.isValidTagPrefix("LLM"));
        }

        @Test
        @DisplayName("'none' is not a prefix: <extend as=\"none\"> means no prefix")
        void none() {
            assertFalse(PackManifest.isValidTagPrefix(PackManifest.NO_PREFIX));
        }

        @Test
        @DisplayName("Empty and null are not prefixes")
        void emptyAndNull() {
            assertFalse(PackManifest.isValidTagPrefix(""));
            assertFalse(PackManifest.isValidTagPrefix(null));
        }
    }
}
