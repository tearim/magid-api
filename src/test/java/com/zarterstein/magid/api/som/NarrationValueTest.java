package com.zarterstein.magid.api.som;

import com.zarterstein.magid.api.reveries.IntegerReverie;
import com.zarterstein.magid.api.reveries.Reverie;
import com.zarterstein.magid.api.values.BasicTypedValue;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mark-Marion Gondelman (Orchestration)
 * @author Code: Claude Code (agentic implementation)
 */
class NarrationValueTest {

    @Test
    void stringHasStringType() {
        assertEquals(BasicTypedValue.STRING, NarrationValue.of("duke").getType());
    }

    @Test
    void integerHasIntegerType() {
        assertEquals(BasicTypedValue.INTEGER, NarrationValue.of(320).getType());
    }

    @Test
    void floatHasFloatType() {
        assertEquals(BasicTypedValue.FLOAT, NarrationValue.of(0.5f).getType());
    }

    @Test
    void booleanHasBooleanType() {
        assertEquals(BasicTypedValue.BOOLEAN, NarrationValue.of(true).getType());
    }

    @Test
    void getReturnsTheValue() {
        assertEquals(320, NarrationValue.of(320).get());
    }

    @Test
    void otherTypeIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new NarrationValue<>(4.2d));
    }

    @Test
    void nullIsRejected() {
        assertThrows(NullPointerException.class, () -> new NarrationValue<>(null));
    }

    @Test
    void equalValuesAreEqual() {
        assertEquals(NarrationValue.of("duke"), NarrationValue.of("duke"));
    }

    @Test
    void typeNamesMatchReveries() {
        assertEquals(IntegerReverie.TYPE, NarrationValue.of(1).getType());
    }

    @Test
    void reverieIsABasicTypedValue() {
        BasicTypedValue<Integer> value = Reverie.of(42);
        assertEquals(BasicTypedValue.INTEGER, value.getType());
    }

    @Test
    void narrationMapTakesNarrationValuesAndReveries() {
        Map<String, BasicTypedValue<?>> line = Map.of(
                "text", NarrationValue.of("I am pleased with your work"),
                "width", Reverie.of(320));
        assertEquals(2, line.size());
    }
}
