package com.zarterstein.magid.api.reveries;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The built-in reveries: {@link Reverie#of} picks the record, the type names are the ones saves store, values are
 * compared by value, never {@code null}, and print as their text.
 *
 * @author Mark-Marion Gondelman (Orchestration)
 * @author Code: Claude Code (agentic implementation)
 */
class ReverieTest {

    @Test
    @DisplayName("of(boolean) gives a BooleanReverie of type boolean")
    void ofBoolean() {
        Reverie<Boolean> r = Reverie.of(true);
        assertInstanceOf(BooleanReverie.class, r);
        assertEquals("boolean", r.getType());
        assertEquals(true, r.get());
    }

    @Test
    @DisplayName("of(int) gives an IntegerReverie of type integer")
    void ofInt() {
        Reverie<Integer> r = Reverie.of(42);
        assertInstanceOf(IntegerReverie.class, r);
        assertEquals("integer", r.getType());
        assertEquals(42, r.get());
    }

    @Test
    @DisplayName("of(float) gives a FloatReverie of type float")
    void ofFloat() {
        Reverie<Float> r = Reverie.of(0.5f);
        assertInstanceOf(FloatReverie.class, r);
        assertEquals("float", r.getType());
        assertEquals(0.5f, r.get());
    }

    @Test
    @DisplayName("of(String) keeps the text as it is: \"42\" stays a string")
    void ofStringKeepsTheText() {
        Reverie<String> r = Reverie.of("42");
        assertInstanceOf(StringReverie.class, r);
        assertEquals("string", r.getType());
        assertEquals("42", r.get());
    }

    @Test
    @DisplayName("Reveries with equal values of the same type are equal and hash alike")
    void equalValuesAreEqual() {
        assertEquals(Reverie.of(42), Reverie.of(42));
        assertEquals(Reverie.of(42).hashCode(), Reverie.of(42).hashCode());
        assertEquals(Reverie.of("Ada"), new StringReverie("Ada"));
    }

    @Test
    @DisplayName("The same number as integer and as float are different reveries")
    void typesDiffer() {
        assertNotEquals(Reverie.of(42), Reverie.of(42f));
    }

    @Test
    @DisplayName("No reverie holds null")
    void nullIsRejected() {
        assertAll(
                () -> assertThrows(NullPointerException.class, () -> new BooleanReverie(null)),
                () -> assertThrows(NullPointerException.class, () -> new IntegerReverie(null)),
                () -> assertThrows(NullPointerException.class, () -> new FloatReverie(null)),
                () -> assertThrows(NullPointerException.class, () -> new StringReverie(null)),
                () -> assertThrows(NullPointerException.class, () -> Reverie.of((String) null)));
    }

    @Test
    @DisplayName("toString is the value's text, which narration shows")
    void toStringIsTheValue() {
        assertAll(
                () -> assertEquals("true", Reverie.of(true).toString()),
                () -> assertEquals("42", Reverie.of(42).toString()),
                () -> assertEquals("0.5", Reverie.of(0.5f).toString()),
                () -> assertEquals("Ada", Reverie.of("Ada").toString()));
    }
}
