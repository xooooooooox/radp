package space.x9x.radp.spring.boot.bootstrap;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import space.x9x.radp.commons.lang.StringUtils;
import space.x9x.radp.commons.lang.Strings;
import space.x9x.radp.extension.ExtensionLoader;
import space.x9x.radp.spring.boot.bootstrap.env.EnvironmentInboundParser;
import space.x9x.radp.spring.boot.bootstrap.env.EnvironmentOutboundParser;
import space.x9x.radp.spring.boot.bootstrap.util.SpringBootProfileUtils;
import space.x9x.radp.spring.framework.bootstrap.constant.SpringProperties;
import space.x9x.radp.spring.framework.bootstrap.utils.SpringProfileUtils;

import java.util.Set;

/**
 * @author x9x
 * @since 2024-09-28 21:33
 */
@Slf4j
@UtilityClass
public class SpringBootApplicationHelper {

    /**
     * Runs a Spring Boot application with the specified parameters.
     * This method sets necessary system properties, configures the application,
     * adds default profiles, runs the application, and logs information after startup.
     *
     * @param mainClass          the main application class to run
     * @param args               the command line arguments
     * @param webApplicationType the type of web application to create (NONE, SERVLET, REACTIVE)
     */
    public static void run(Class<?> mainClass, String[] args, WebApplicationType webApplicationType) {
        setSystemProperties();
        try {
            SpringApplication app = new SpringApplicationBuilder(mainClass).web(webApplicationType).build();
            SpringBootProfileUtils.addDefaultProfile(app);

            ConfigurableApplicationContext context = app.run(args);
            ConfigurableEnvironment env = context.getEnvironment();
            logAfterRunning(env);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private static void setSystemProperties() {
        // Fixed elasticsearch error: availableProcessors is already set to [], rejecting []
        System.setProperty("es.set.netty.runtime.available.processors", "false");

        // Fixed dubbo error: No such any registry to refer service in consumer xxx.xxx.xxx.xxx use dubbo version 2.x.x
        System.setProperty("java.net.preferIPv4Stack", "true");

        // Fixed zookeeper error: java.io.IOException: Unreasonable length = 1048575
        System.setProperty("jute.maxbuffer", String.valueOf(8192 * 1024));
    }

    private static void logAfterRunning(Environment env) {
        String appName = StringUtils.trimToEmpty(env.getProperty(SpringProperties.SPRING_APPLICATION_NAME));
        String profile = String.join(Strings.COMMA, SpringProfileUtils.getActiveProfiles(env));
        log.info("\n----------------------------------------------------------\n"
                        + "Application '{}' is running!\n"
                        + "Profile(s):\t{}\n"
                        + getInboundInfo(env)
                        + getOutboundInfo(env)
                        + "----------------------------------------------------------",
                appName,
                profile);
    }

    private String getInboundInfo(Environment env) {
        ExtensionLoader<EnvironmentInboundParser> extensionLoader = ExtensionLoader.getExtensionLoader(EnvironmentInboundParser.class);
        Set<String> extensions = extensionLoader.getSupportedExtensions();
        StringBuilder logStr = new StringBuilder();
        for (String extension : extensions) {
            String info = extensionLoader.getExtension(extension).toString(env);
            if (StringUtils.isBlank(info)) {
                continue;
            }
            logStr.append(info).append("\n");
        }
        return logStr.toString();
    }

    private String getOutboundInfo(Environment env) {
        ExtensionLoader<EnvironmentOutboundParser> extensionLoader = ExtensionLoader.getExtensionLoader(EnvironmentOutboundParser.class);
        Set<String> extensions = extensionLoader.getSupportedExtensions();
        StringBuilder logStr = new StringBuilder();
        for (String extension : extensions) {
            String info = extensionLoader.getExtension(extension).toString(env);
            if (StringUtils.isBlank(info)) {
                continue;
            }
            logStr.append(info).append("\n");
        }
        return logStr.toString();
    }
}
