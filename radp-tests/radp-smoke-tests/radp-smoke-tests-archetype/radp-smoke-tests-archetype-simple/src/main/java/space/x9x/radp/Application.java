package space.x9x.radp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import space.x9x.radp.spring.framework.web.rest.annotation.EnableRestExceptionHandler;

/**
 * @author IO x9x
 * @since 2024-10-11 16:33
 */
@SpringBootApplication
@EnableRestExceptionHandler
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
