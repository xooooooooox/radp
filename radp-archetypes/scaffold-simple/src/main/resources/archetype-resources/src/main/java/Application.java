#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import space.x9x.radp.spring.framework.web.rest.annotation.EnableRestExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
