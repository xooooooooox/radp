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
        containerType   | containerMethod                              | imageName
        "Redis"         | { ContainerHelper.redisContainer() }         | "redis"
        "MySQL8"        | { ContainerHelper.mysql8Container() }        | "mysql"
        "Zookeeper"     | { ContainerHelper.zookeeperContainer() }     | "zookeeper"
        "Kafka"         | { ContainerHelper.kafkaContainer() }         | "kafka"
        "Elasticsearch" | { ContainerHelper.elasticsearchContainer() } | "elasticsearch"
        "MongoDB"       | { ContainerHelper.mongoDBContainer() }       | "mongo"
        "Nginx"         | { ContainerHelper.nginxContainer() }         | "nginx"
        "MariaDB"       | { ContainerHelper.mariaDBContainer() }       | "mariadb"
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

    def "test create Elasticsearch container with custom image"() {
        given:
        def customImage = "docker.elastic.co/elasticsearch/elasticsearch:8.6.0"

        when:
        def container = ContainerHelper.elasticsearchContainer(customImage)

        then:
        container != null
        container.getDockerImageName().toString() == customImage
    }

    def "test create Elasticsearch container with custom image and timeout"() {
        given:
        def imageName = DockerImageName.parse("docker.elastic.co/elasticsearch/elasticsearch:8.6.0")
        def timeout = Duration.ofSeconds(180)

        when:
        def container = ContainerHelper.elasticsearchContainer(imageName, timeout)

        then:
        container != null
        container.getDockerImageName().toString() == imageName.toString()
    }

    def "test create MongoDB container with custom image"() {
        given:
        def customImage = "mongo:6.0.0"

        when:
        def container = ContainerHelper.mongoDBContainer(customImage)

        then:
        container != null
        container.getDockerImageName().toString() == customImage
    }

    def "test create MongoDB container with custom image and timeout"() {
        given:
        def imageName = DockerImageName.parse("mongo:6.0.0")
        def timeout = Duration.ofSeconds(90)

        when:
        def container = ContainerHelper.mongoDBContainer(imageName, timeout)

        then:
        container != null
        container.getDockerImageName().toString() == imageName.toString()
    }

    def "test create Nginx container with custom image"() {
        given:
        def customImage = "nginx:1.24.0"

        when:
        def container = ContainerHelper.nginxContainer(customImage)

        then:
        container != null
        container.getDockerImageName().toString() == customImage
    }

    def "test create Nginx container with custom image and timeout"() {
        given:
        def imageName = DockerImageName.parse("nginx:1.24.0")
        def timeout = Duration.ofSeconds(45)

        when:
        def container = ContainerHelper.nginxContainer(imageName, timeout)

        then:
        container != null
        container.getDockerImageName().toString() == imageName.toString()
    }

    def "test create MariaDB container with custom image"() {
        given:
        def customImage = "mariadb:11.0.0"

        when:
        def container = ContainerHelper.mariaDBContainer(customImage)

        then:
        container != null
        container.getDockerImageName().toString() == customImage
    }

    def "test create MariaDB container with custom image and timeout"() {
        given:
        def imageName = DockerImageName.parse("mariadb:11.0.0")
        def timeout = Duration.ofSeconds(90)

        when:
        def container = ContainerHelper.mariaDBContainer(imageName, timeout)

        then:
        container != null
        container.getDockerImageName().toString() == imageName.toString()
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
