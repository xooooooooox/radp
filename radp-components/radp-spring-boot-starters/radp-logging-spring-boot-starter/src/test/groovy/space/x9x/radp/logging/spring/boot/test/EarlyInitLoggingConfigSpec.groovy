package space.x9x.radp.logging.spring.boot.test

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.joran.JoranConfigurator
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.Appender
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.read.ListAppender
import ch.qos.logback.core.util.StatusPrinter
import org.slf4j.LoggerFactory
import spock.lang.Specification

/**
 * 早期初始化场景日志配置测试
 *
 * 这个测试类验证早期初始化场景的日志配置是否正确，包括：
 * 1. 在 Spring Boot 初始化前是否能正确格式化日志
 * 2. 控制台日志输出格式
 * 3. 文件日志输出
 *
 * 注意：这个测试类不使用 Spring Boot 的自动配置，而是手动配置日志系统，
 * 以模拟早期初始化场景（在 Spring Boot 完全初始化之前）。
 */
class EarlyInitLoggingConfigSpec extends Specification {

    def setup() {
        // 手动配置 Logback，模拟早期初始化场景
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory()
        context.reset()

        JoranConfigurator configurator = new JoranConfigurator()
        configurator.setContext(context)

        // 加载测试配置文件
        URL configURL = this.class.getClassLoader().getResource("early-init/logback-test.xml")
        configurator.doConfigure(configURL)

        // 打印 Logback 内部状态，用于调试
        StatusPrinter.printInCaseOfErrorsOrWarnings(context)
    }

    /**
     * 测试早期初始化场景的日志配置是否正确加载
     */
    def "should load early initialization logging configuration correctly"() {
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
     * 测试控制台日志格式是否使用标准的 Logback 模式
     */
    def "should use standard Logback pattern for console output"() {
        given: "获取根日志记录器和控制台附加器"
        Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME)
        ConsoleAppender<ILoggingEvent> consoleAppender = findAppender(rootLogger, ConsoleAppender.class)

        expect: "控制台附加器应该存在"
        consoleAppender != null

        and: "控制台附加器的模式不应该包含 Spring Boot 特有的转换模式"
        def pattern = consoleAppender.encoder.pattern
        println "[DEBUG_LOG] Early init console pattern: ${pattern}"
        !pattern.contains("%clr")
        !pattern.contains("%wEx")
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
