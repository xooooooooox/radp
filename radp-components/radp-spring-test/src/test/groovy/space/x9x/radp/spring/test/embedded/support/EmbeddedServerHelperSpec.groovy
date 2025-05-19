package space.x9x.radp.spring.test.embedded.support

import space.x9x.radp.spring.test.embedded.EmbeddedServer
import space.x9x.radp.spring.test.embedded.kafka.EmbeddedKafkaServer
import space.x9x.radp.spring.test.embedded.redis.EmbeddedRedisServer
import space.x9x.radp.spring.test.embedded.zookeeper.EmbeddedZookeeperServer
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Spock tests for {@link EmbeddedServerHelper}.
 *
 * @author x9x
 * @since 2024-10-30
 */
class EmbeddedServerHelperSpec extends Specification {

    @Unroll
    def "test create #serverType server"() {
        when:
        def server = serverMethod.call()

        then:
        server != null
        server.class == serverClass

        where:
        serverType  | serverMethod                               | serverClass
        "Redis"     | { EmbeddedServerHelper.redisServer() }     | EmbeddedRedisServer
        "Zookeeper" | { EmbeddedServerHelper.zookeeperServer() } | EmbeddedZookeeperServer
        "Kafka"     | { EmbeddedServerHelper.kafkaServer() }     | EmbeddedKafkaServer
    }

    def "test create Redis server with custom port"() {
        given:
        def customPort = 6380

        when:
        def server = EmbeddedServerHelper.redisServer(customPort)

        then:
        server != null
        server instanceof EmbeddedRedisServer
    }

    def "test create Redis server with custom port and password"() {
        given:
        def customPort = 6380
        def password = "testpassword"

        when:
        def server = EmbeddedServerHelper.redisServer(customPort, password)

        then:
        server != null
        server instanceof EmbeddedRedisServer
    }

    def "test create Kafka server with custom port"() {
        given:
        def customPort = 9093

        when:
        def server = EmbeddedServerHelper.kafkaServer(customPort)

        then:
        server != null
        server instanceof EmbeddedKafkaServer
    }

    def "test create Kafka server with custom Kafka port and Zookeeper port"() {
        given:
        def kafkaPort = 9093
        def zookeeperPort = 2182

        when:
        def server = EmbeddedServerHelper.kafkaServer(kafkaPort, zookeeperPort)

        then:
        server != null
        server instanceof EmbeddedKafkaServer
    }

    def "test embedded server using extension mechanism"() {
        when:
        def server = EmbeddedServerHelper.embeddedServer("redis", 6380)

        then:
        server != null
        server instanceof EmbeddedServer
    }

    def "test private constructor throws exception"() {
        when:
        def constructor = EmbeddedServerHelper.class.getDeclaredConstructor()
        constructor.setAccessible(true)
        constructor.newInstance()

        then:
        def exception = thrown(Exception)
        exception.cause.message.contains("should not be instantiated")
    }
}