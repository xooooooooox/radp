package space.x9x.radp.smoke.tests.logback.classic;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test for classic Logback configuration.
 */
public class ClassicLogbackTest {

    private static final Logger log = LoggerFactory.getLogger(ClassicLogbackTest.class);

    @BeforeAll
    public static void setup() {
        // Set the logback.configurationFile system property to use our custom configuration
        System.setProperty("logback.configurationFile", "logback-classic.xml");
    }

    @Test
    void test_classicLogging() {
        // Log messages at different levels
        log.trace("This is a TRACE message");
        log.debug("This is a DEBUG message");
        log.info("This is an INFO message");
        log.warn("This is a WARN message");
        log.error("This is an ERROR message");

        // Verify the log file was created at the expected path with the expected filename

        // Verify the log content as expected
    }
}
