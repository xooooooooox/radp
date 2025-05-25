/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package space.x9x.radp.smoke.tests.logging.logback.classic;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;

/**
 * Test for classic Logback configuration.
 */
@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ClassicLogbackTest {

    private LoggerContext loggerCtx;

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
