package space.x9x.radp.logging.spring.boot.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 早期初始化场景日志配置测试应用程序
 * <p>
 * 这是一个简单的 Spring Boot 应用程序，用于测试早期初始化场景的日志配置。
 * 早期初始化场景是指在 Spring Boot 完全初始化之前就需要记录日志的场景，
 * 例如使用 Testcontainers 进行测试时。
 */
@SpringBootApplication
public class EarlyInitLoggingTestApplication {

    public static void main(String[] args) {
        // 在应用程序启动前记录一些日志，模拟早期初始化场景
        System.out.println("Application starting...");
        SpringApplication.run(EarlyInitLoggingTestApplication.class, args);
    }
}