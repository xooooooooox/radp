package space.x9x.radp.smoke.tests.logging.logback.classic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Test for classic Logback configuration.
 */
@Slf4j
public class ClassicLogbackTest {

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
        log.warn("This is a WARN message with exception", new IllegalArgumentException("Something went wrong"));
        log.error("This is a ERROR message with exception", new IllegalArgumentException("Something went wrong"));

        // Verify the log file was created at the expected path with the expected filename

        // Verify the log content as expected
    }
}
