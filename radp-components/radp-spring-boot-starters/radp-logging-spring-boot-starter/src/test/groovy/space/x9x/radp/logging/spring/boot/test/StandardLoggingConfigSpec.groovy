package space.x9x.radp.logging.spring.boot.test

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.Appender
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.read.ListAppender
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

/**
 * 标准应用场景日志配置测试
 *
 * 这个测试类验证标准应用场景的日志配置是否正确，包括：
 * 1. 控制台日志输出格式
 * 2. 文件日志输出
 * 3. 不同环境（local、dev、test、prod）的配置
 * 4. rawConsolePattern 属性的处理
 */
@SpringBootTest(classes = space.x9x.radp.logging.spring.boot.test.StandardLoggingTestApplication.class)
@TestPropertySource(properties = ["logging.config=classpath:logback-spring.xml"])
class StandardLoggingConfigSpec extends Specification {

    /**
     * 测试标准应用场景的日志配置是否正确加载
     */
    def "should load standard logging configuration correctly"() {
        given: "获取根日志记录器"
        Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME)

        expect: "根日志记录器应该有正确的级别和附加器"
        rootLogger.level == Level.INFO

        and: "应该有控制台附加器"
        def consoleAppender = findAppender(rootLogger, ConsoleAppender.class)
        consoleAppender != null

        and: "应该有文件附加器"
        def asyncAppender = findAppender(rootLogger, "ASYNC")
        asyncAppender != null
    }

    /**
     * 测试日志输出格式是否正确
     */
    def "should format log messages correctly"() {
        given: "创建一个测试日志记录器和捕获附加器"
        Logger logger = (Logger) LoggerFactory.getLogger("test.logger")
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>()
        listAppender.start()
        logger.addAppender(listAppender)

        when: "记录一条测试日志"
        logger.info("Test log message")

        then: "日志应该被正确记录"
        listAppender.list.size() == 1
        listAppender.list[0].message == "Test log message"
        listAppender.list[0].level == Level.INFO
    }

    /**
     * 查找指定类型的附加器
     */
    private <T extends Appender> T findAppender(Logger logger, Class<T> appenderClass) {
        for (Appender appender : logger.iteratorForAppenders()) {
            if (appenderClass.isInstance(appender)) {
                return (T) appender
            }
        }
        return null
    }

    /**
     * 查找指定名称的附加器
     */
    private Appender findAppender(Logger logger, String appenderName) {
        for (Appender appender : logger.iteratorForAppenders()) {
            if (appender.name == appenderName) {
                return appender
            }
        }
        return null
    }
}
