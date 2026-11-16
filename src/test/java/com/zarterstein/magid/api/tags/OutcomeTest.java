package com.zarterstein.magid.api.tags;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mark-Marion Gondelman (Orchestration)
 * @author Code: Claude Code (agentic implementation)
 */
class OutcomeTest {

    @Test
    void proceedHasNoTarget() {
        assertEquals(new Outcome(Outcome.Type.PROCEED, null, null), Outcome.proceed());
    }

    @Test
    void jumpKeepsItsScene() {
        Outcome jump = Outcome.jump("castle");
        assertAll(
                () -> assertEquals(Outcome.Type.JUMP, jump.type()),
                () -> assertEquals("castle", jump.sceneId()),
                () -> assertNull(jump.addition()));
    }

    @Test
    void actKeepsSceneAndTrigger() {
        Outcome act = Outcome.act("hall", "door");
        assertAll(
                () -> assertEquals(Outcome.Type.ACT, act.type()),
                () -> assertEquals("hall", act.sceneId()),
                () -> assertEquals("door", act.addition()));
    }

    @Test
    void stayRerunsOnEntryAndItsVariantDoesNot() {
        assertEquals(Outcome.Type.STAY, Outcome.stay().type());
        assertEquals(Outcome.Type.STAY_WITHOUT_ON_ENTRY, Outcome.stayWithoutOnEntry().type());
    }

    @Test
    void endingKeepsItsName() {
        assertEquals("pit", Outcome.die("pit").addition());
    }

    @Test
    void nullEndingIsTheGenericEnding() {
        assertEquals("", Outcome.win(null).addition());
    }

    @Test
    void jumpWithoutSceneIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> Outcome.jump(" "));
    }

    @Test
    void actWithoutTriggerIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> Outcome.act("hall", null));
    }

    @Test
    void proceedWithTargetIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new Outcome(Outcome.Type.PROCEED, "hall", null));
    }

    @Test
    void endingWithSceneIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new Outcome(Outcome.Type.END, "hall", "x"));
    }

    @Test
    void nullTypeIsRejected() {
        assertThrows(NullPointerException.class, () -> new Outcome(null, null, null));
    }

    // ─── TagResult ──────────────────────────────────────────────────────────

    @Test
    void textResultProceeds() {
        assertEquals(new TagResult("The Duke nods.", Outcome.proceed()), TagResult.text("The Duke nods."));
    }

    @Test
    void outcomeResultHasNoText() {
        assertEquals("", TagResult.of(Outcome.jump("castle")).text());
    }

    @Test
    void nullTextBecomesEmpty() {
        assertEquals("", new TagResult(null, Outcome.proceed()).text());
    }

    @Test
    void nullOutcomeIsRejected() {
        assertThrows(NullPointerException.class, () -> new TagResult("x", null));
    }
}
