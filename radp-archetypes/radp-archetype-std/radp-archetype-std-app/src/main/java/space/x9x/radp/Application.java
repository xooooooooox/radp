package space.x9x.radp;

import space.x9x.radp.spring.framework.web.rest.annotation.EnableRestExceptionHandler;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Configurable
@EnableRestExceptionHandler
public class Application {

    public static void main(String[] args){
        SpringApplication.run(Application.class);
    }

}
