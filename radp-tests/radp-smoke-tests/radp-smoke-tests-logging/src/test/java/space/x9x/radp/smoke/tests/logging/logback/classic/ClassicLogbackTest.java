package space.x9x.radp.smoke.tests.logging.logback.classic;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

/**
 * Test for classic Logback configuration.
 */
@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClassicLogbackTest {

    private LoggerContext loggerCtx;
    private Path logFile;

    @BeforeAll
    void setup() throws IOException, JoranException {
        loggerCtx = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerCtx.reset();

        URL configUrl = getClass().getClassLoader().getResources("logback-classic.xml").nextElement();
        Assertions.assertNotNull(configUrl, "无法找到 logback-classic.xml");
        JoranConfigurator cfg = new JoranConfigurator();
        cfg.setContext(loggerCtx);
        cfg.doConfigure(configUrl);
    }

    @AfterAll
    void tearDown() {
        if (loggerCtx != null) {
            loggerCtx.stop();
        }
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
