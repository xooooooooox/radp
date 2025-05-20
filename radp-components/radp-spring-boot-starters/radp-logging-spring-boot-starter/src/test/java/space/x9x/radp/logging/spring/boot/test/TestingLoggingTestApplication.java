package space.x9x.radp.logging.spring.boot.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 测试环境场景日志配置测试应用程序
 * <p>
 * 这是一个简单的 Spring Boot 应用程序，用于测试测试环境场景的日志配置。
 */
@SpringBootApplication
public class TestingLoggingTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestingLoggingTestApplication.class, args);
    }
}