package space.x9x.radp.smoke.tests.logging.logback.springboot;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import space.x9x.radp.smoke.tests.logging.logback.LoggingApplication;

/**
 * Test for Spring Boot integration with Logback.
 */
@SpringBootTest(classes = LoggingApplication.class)
@Slf4j
class SpringBootLogbackTest {


    @Test
    void test_springBootLogging() {
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
