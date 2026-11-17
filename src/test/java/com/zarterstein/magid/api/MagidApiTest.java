package com.zarterstein.magid.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link MagidApi}.
 *
 * @author Mark-Marion Gondelman (Orchestration)
 * @author Code: Claude Code (agentic implementation)
 */
class MagidApiTest {

    @Test
    @DisplayName("MagidApi.version() is the pom's version")
    void versionMatchesPom() throws IOException {
        String pomVersion;
        try (InputStream in = MagidApiTest.class.getResourceAsStream("/magid-api.pom.version")) {
            assertNotNull(in, "precondition: Maven filtered the test resource");
            pomVersion = new String(in.readAllBytes(), StandardCharsets.UTF_8).trim();
        }
        assertFalse(pomVersion.contains("${"), "precondition: the test resource is filtered");

        assertEquals(pomVersion, MagidApi.version());
    }

    @Test
    @DisplayName("MagidApi.PACKAGE is this class's package")
    void packageMatches() {
        assertEquals(MagidApi.class.getPackageName() + ".", MagidApi.PACKAGE);
    }
}
