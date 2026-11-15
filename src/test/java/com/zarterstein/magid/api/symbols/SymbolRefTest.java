package com.zarterstein.magid.api.symbols;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link SymbolRef#of}: one test per written form, then the malformed ones.
 *
 * @author Mark-Marion Gondelman (Orchestration)
 * @author Code: Claude Code (agentic implementation)
 */
class SymbolRefTest {

    @Test
    @DisplayName("$gold is the variable gold")
    void variable() {
        assertEquals(new SymbolRef(SymbolRef.Type.VARIABLE, "", "gold"), SymbolRef.of("$gold"));
    }

    @Test
    @DisplayName("#maxHp is the constant maxHp")
    void constant() {
        assertEquals(new SymbolRef(SymbolRef.Type.CONSTANT, "", "maxHp"), SymbolRef.of("#maxHp"));
    }

    @Test
    @DisplayName("^butler is a reference to butler")
    void reference() {
        assertEquals(new SymbolRef(SymbolRef.Type.REFERENCE, "", "butler"), SymbolRef.of("^butler"));
    }

    @Test
    @DisplayName("player.gold is the memory gold of player")
    void memory() {
        assertEquals(new SymbolRef(SymbolRef.Type.MEMORY, "player", "gold"), SymbolRef.of("player.gold"));
    }

    @Test
    @DisplayName("player.inventory.sword: the key keeps everything after the first dot")
    void memoryKeyKeepsFurtherDots() {
        assertEquals(new SymbolRef(SymbolRef.Type.MEMORY, "player", "inventory.sword"), SymbolRef.of("player.inventory.sword"));
    }

    @Test
    @DisplayName("hero::title is the attribute title of hero")
    void attribute() {
        assertEquals(new SymbolRef(SymbolRef.Type.ATTRIBUTE, "hero", "title"), SymbolRef.of("hero::title"));
    }

    @Test
    @DisplayName("hero::title.x is an attribute whose key is title.x")
    void attributeWinsOverMemory() {
        assertEquals(new SymbolRef(SymbolRef.Type.ATTRIBUTE, "hero", "title.x"), SymbolRef.of("hero::title.x"));
    }

    @Test
    @DisplayName("{player.gold} is player.gold without the braces")
    void braces() {
        assertEquals(new SymbolRef(SymbolRef.Type.MEMORY, "player", "gold"), SymbolRef.of("{player.gold}"));
    }

    @Test
    @DisplayName("A bare name is a variable (DECISIONS.md D14)")
    void bareNameIsVariable() {
        assertEquals(new SymbolRef(SymbolRef.Type.VARIABLE, "", "gold"), SymbolRef.of("gold"));
    }

    @Test
    @DisplayName("Surrounding spaces are ignored")
    void surroundingSpacesIgnored() {
        assertEquals(new SymbolRef(SymbolRef.Type.MEMORY, "player", "gold"), SymbolRef.of(" player.gold "));
    }

    @Test
    @DisplayName("$ alone is an error")
    void variableWithoutName() {
        assertEquals(new SymbolRef(SymbolRef.Type.ERROR, "", "$"), SymbolRef.of("$"));
    }

    @Test
    @DisplayName("# alone is an error")
    void constantWithoutName() {
        assertEquals(new SymbolRef(SymbolRef.Type.ERROR, "", "#"), SymbolRef.of("#"));
    }

    @Test
    @DisplayName("^ alone is an error")
    void referenceWithoutName() {
        assertEquals(new SymbolRef(SymbolRef.Type.ERROR, "", "^"), SymbolRef.of("^"));
    }

    @Test
    @DisplayName("player. (no key) is an error")
    void memoryWithoutKey() {
        assertEquals(new SymbolRef(SymbolRef.Type.ERROR, "", "player."), SymbolRef.of("player."));
    }

    @Test
    @DisplayName(".gold (no owner) is an error")
    void memoryWithoutOwner() {
        assertEquals(new SymbolRef(SymbolRef.Type.ERROR, "", ".gold"), SymbolRef.of(".gold"));
    }

    @Test
    @DisplayName("player:: (no key) is an error")
    void attributeWithoutKey() {
        assertEquals(new SymbolRef(SymbolRef.Type.ERROR, "", "player::"), SymbolRef.of("player::"));
    }

    @Test
    @DisplayName("::title (no owner) is an error")
    void attributeWithoutOwner() {
        assertEquals(new SymbolRef(SymbolRef.Type.ERROR, "", "::title"), SymbolRef.of("::title"));
    }

    @Test
    @DisplayName("{} is an error")
    void emptyBraces() {
        assertEquals(new SymbolRef(SymbolRef.Type.ERROR, "", ""), SymbolRef.of("{}"));
    }

    @Test
    @DisplayName("null is an error, not an exception")
    void nullInput() {
        assertEquals(new SymbolRef(SymbolRef.Type.ERROR, "", null), SymbolRef.of(null));
    }
}
