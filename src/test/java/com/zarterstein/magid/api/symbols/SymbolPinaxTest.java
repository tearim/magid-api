package com.zarterstein.magid.api.symbols;

import com.zarterstein.magid.api.reveries.Reverie;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The default readers of {@link SymbolPinax}: {@code get} and {@code getString}, over a fake whose only real method is
 * {@code recall}.
 *
 * @author Mark-Marion Gondelman (Orchestration)
 * @author Code: Claude Code (agentic implementation)
 */
class SymbolPinaxTest {

    private static final Map<String, Reverie<?>> SYMBOLS = Map.of(
            "player.gold", Reverie.of(42),
            "$ratio", Reverie.of(2.5f),
            "$door-open", Reverie.of(true),
            "player::title", Reverie.of("Sir"),
            "$code", Reverie.of("007"));

    /** Answers {@code recall} from {@link #SYMBOLS}; everything else is unused here. */
    private final SymbolPinax pinax = new SymbolPinax() {
        public Optional<Reverie<?>> recall(String symbol) { return Optional.ofNullable(SYMBOLS.get(symbol)); }
        public void set(String symbol, Object value) { throw new UnsupportedOperationException(); }
        public void declare(String symbol, Object value) { throw new UnsupportedOperationException(); }
        public void clear(String symbol) { throw new UnsupportedOperationException(); }
        public boolean has(String symbol) { return SYMBOLS.containsKey(symbol); }
        public SymbolRef info(String symbol) { throw new UnsupportedOperationException(); }
        public void markTemporary(String symbol, Scope scope) { throw new UnsupportedOperationException(); }
        public boolean isDynamicCastingAllowed() { return false; }
        public boolean areNonDeclaredSymbolsAllowed() { return false; }
        public ErrorReactionLevel getDynamicCastLevel() { return ErrorReactionLevel.NONE; }
        public ErrorReactionLevel getNonDeclaredLevel() { return ErrorReactionLevel.NONE; }
    };

    // ─── get ────────────────────────────────────────────────────────────────

    @Test
    void getGivesAnIntegerAsInteger() {
        assertEquals(42, pinax.get("player.gold"));
    }

    @Test
    void getGivesABooleanAsBoolean() {
        assertEquals(Boolean.TRUE, pinax.get("$door-open"));
    }

    @Test
    void getGivesAStringAsString() {
        assertEquals("007", pinax.get("$code"));
    }

    @Test
    void getOfAMissingSymbolIsNull() {
        assertNull(pinax.get("player.silver"));
    }

    // ─── getString ──────────────────────────────────────────────────────────

    @Test
    void getStringGivesAnIntegerAsText() {
        assertEquals("42", pinax.getString("player.gold"));
    }

    @Test
    void getStringGivesAFloatAsText() {
        assertEquals("2.5", pinax.getString("$ratio"));
    }

    @Test
    void getStringGivesAnAttribute() {
        assertEquals("Sir", pinax.getString("player::title"));
    }

    @Test
    void getStringOfAMissingSymbolIsEmpty() {
        assertEquals("", pinax.getString("player.silver"));
    }

    @Test
    void getStringWithFallbackGivesTheValueWhenItExists() {
        assertEquals("42", pinax.getString("player.gold", "none"));
    }

    @Test
    void getStringWithFallbackGivesTheFallbackWhenMissing() {
        assertEquals("none", pinax.getString("player.silver", "none"));
    }
}
