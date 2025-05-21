package space.x9x.radp.smoke.tests.logging.logback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author x9x
 * @since 2025-05-21 01:21
 */
@SpringBootApplication(
        scanBasePackages = {
                "space.x9x.radp.smoke.tests.logging.logback.springboot"
        }
)
public class LoggingApplication {
    public static void main(String[] args) {
        SpringApplication.run(LoggingApplication.class, args);
    }
}
