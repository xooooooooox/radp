package space.x9x.radp.spring.test.container.support

import org.testcontainers.utility.DockerImageName
import spock.lang.Specification
import spock.lang.Unroll

import java.time.Duration

/**
 * Spock tests for {@link ContainerHelper}.
 *
 * @author x9x
 * @since 2024-10-30
 */
class ContainerHelperSpec extends Specification {

    @Unroll
    def "test create #containerType container with default settings"() {
        when:
        def container = containerMethod.call()

        then:
        container != null
        container.getDockerImageName().toString().contains(imageName)

        where:
        containerType | containerMethod                          | imageName
        "Redis"       | { ContainerHelper.redisContainer() }     | "redis"
        "MySQL8"      | { ContainerHelper.mysql8Container() }    | "mysql"
        "Zookeeper"   | { ContainerHelper.zookeeperContainer() } | "zookeeper"
        "Kafka"       | { ContainerHelper.kafkaContainer() }     | "kafka"
    }

    def "test create Redis container with custom image"() {
        given:
        def customImage = "redis:7.0"

        when:
        def container = ContainerHelper.redisContainer(customImage)

        then:
        container != null
        container.getDockerImageName().toString() == customImage
    }

    def "test create MySQL8 container with custom image"() {
        given:
        def customImage = "mysql:8.0.33"

        when:
        def container = ContainerHelper.mysql8Container(customImage)

        then:
        container != null
        container.getDockerImageName().toString() == customImage
    }

    def "test create MySQL8 container with custom image and timeout"() {
        given:
        def imageName = DockerImageName.parse("mysql:8.0.33")
        def timeout = Duration.ofSeconds(120)

        when:
        def container = ContainerHelper.mysql8Container(imageName, timeout)

        then:
        container != null
        container.getDockerImageName().toString() == imageName.toString()
    }

    def "test create Zookeeper container with custom version"() {
        given:
        def customVersion = "3.7.1"

        when:
        def container = ContainerHelper.zookeeperContainer(customVersion)

        then:
        container != null
        container.getDockerImageName().toString().contains(customVersion)
    }

    def "test create Kafka container with custom version"() {
        given:
        def customVersion = "7.3.0"

        when:
        def container = ContainerHelper.kafkaContainer(customVersion)

        then:
        container != null
        container.getDockerImageName().toString().contains(customVersion)
    }

    def "test private constructor throws exception"() {
        when:
        def constructor = ContainerHelper.class.getDeclaredConstructor()
        constructor.setAccessible(true)
        constructor.newInstance()

        then:
        def exception = thrown(Exception)
        exception.cause.message.contains("should not be instantiated")
    }
}