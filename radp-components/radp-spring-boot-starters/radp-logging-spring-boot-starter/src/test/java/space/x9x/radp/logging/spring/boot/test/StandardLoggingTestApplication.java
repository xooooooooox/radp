package space.x9x.radp.logging.spring.boot.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 标准应用场景日志配置测试应用程序
 * <p>
 * 这是一个简单的 Spring Boot 应用程序，用于测试标准应用场景的日志配置。
 */
@SpringBootApplication
public class StandardLoggingTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(StandardLoggingTestApplication.class, args);
    }
}