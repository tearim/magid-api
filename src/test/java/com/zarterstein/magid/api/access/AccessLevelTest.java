package com.zarterstein.magid.api.access;

import com.zarterstein.magid.api.tags.TagContext;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link AccessLevel}, {@link Caller#NONE}, {@link CallerSource#NONE} and the default of {@link TagContext#caller()}.
 *
 * @author Mark-Marion Gondelman (Orchestration)
 * @author Code: Claude Code (agentic implementation)
 */
class AccessLevelTest {

    @Test
    void aHigherLevelIsAtLeastALowerOne() {
        assertTrue(AccessLevel.MODERATOR.atLeast(AccessLevel.USER));
    }

    @Test
    void aLevelIsAtLeastItself() {
        assertTrue(AccessLevel.USER.atLeast(AccessLevel.USER));
    }

    @Test
    void aLowerLevelIsNotAtLeastAHigherOne() {
        assertFalse(AccessLevel.ANON.atLeast(AccessLevel.USER));
    }

    @Test
    void parseIgnoresCaseAndSpaces() {
        assertEquals(AccessLevel.ADMIN, AccessLevel.parse(" admin "));
    }

    @Test
    void parseRefusesAnUnknownNameAndNamesIt() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> AccessLevel.parse("wizard"));
        assertTrue(e.getMessage().contains("wizard"));
    }

    @Test
    void parseRefusesNull() {
        assertThrows(IllegalArgumentException.class, () -> AccessLevel.parse(null));
    }

    @Test
    void theNoneCallerIsAnonymousWithAFixedIdNoLocaleAndNoGroups() {
        Caller caller = Caller.NONE;
        assertEquals(AccessLevel.ANON, caller.level());
        assertEquals(Caller.NONE_ID, caller.pseudonymousId());
        assertTrue(caller.locale().isEmpty());
        assertFalse(caller.inGroup("verified-adults"));
    }

    @Test
    void theNoneSourceGivesEveryPackTheNoneCaller() {
        assertSame(Caller.NONE, CallerSource.NONE.forPack("magid-ollama-pack"));
    }

    @Test
    void aContextWithoutAnAccountGivesTheNoneCaller() {
        // a fake context implementing only the abstract methods (all of them unused here)
        TagContext context = (TagContext) Proxy.newProxyInstance(TagContext.class.getClassLoader(),
                new Class<?>[]{TagContext.class}, (proxy, method, args) -> {
                    if (method.isDefault()) {
                        return java.lang.reflect.InvocationHandler.invokeDefault(proxy, method, args);
                    }
                    throw new UnsupportedOperationException(method.getName());
                });
        assertSame(Caller.NONE, context.caller());
    }
}
