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
 * 这个测试类用于测试EmbeddedServerHelper工具类的各种嵌入式服务器创建方法。
 * 测试包括使用默认设置和自定义设置创建各种类型的嵌入式服务器，如Redis、Kafka、Zookeeper等。
 * 同时也测试了私有构造函数的异常抛出情况。
 *
 * @author x9x
 * @since 2024-10-30
 */
class EmbeddedServerHelperSpec extends Specification {

    /**
     * 测试使用默认设置创建各种类型的嵌入式服务器
     *
     * 这个测试方法验证EmbeddedServerHelper能够使用默认设置成功创建不同类型的嵌入式服务器，
     * 并检查创建的服务器是否为预期的类型。
     */
    @Unroll
    def "test create #serverType server with default settings"() {
        when: "调用服务器创建方法"
        def server = serverMethod.call()

        then: "验证服务器创建成功且类型正确"
        server != null
        server.class == serverClass

        where: "测试不同类型的服务器"
        serverType  | serverMethod                               | serverClass
        "Redis"     | { EmbeddedServerHelper.redisServer() }     | EmbeddedRedisServer
        "Zookeeper" | { EmbeddedServerHelper.zookeeperServer() } | EmbeddedZookeeperServer
        "Kafka"     | { EmbeddedServerHelper.kafkaServer() }     | EmbeddedKafkaServer
    }

    /**
     * 测试使用自定义端口创建Redis服务器
     *
     * 这个测试方法验证EmbeddedServerHelper能够使用自定义端口成功创建Redis服务器，
     * 并检查创建的服务器是否为正确的类型。
     */
    def "test create Redis server with custom port"() {
        given: "准备自定义端口"
        def customPort = 6380

        when: "使用自定义端口创建Redis服务器"
        def server = EmbeddedServerHelper.redisServer(customPort)

        then: "验证服务器创建成功且类型正确"
        server != null
        server instanceof EmbeddedRedisServer
    }

    /**
     * 测试使用自定义端口和密码创建Redis服务器
     *
     * 这个测试方法验证EmbeddedServerHelper能够使用自定义端口和密码成功创建Redis服务器，
     * 并检查创建的服务器是否为正确的类型。
     */
    def "test create Redis server with custom port and password"() {
        given: "准备自定义端口和密码"
        def customPort = 6380
        def password = "testpassword"

        when: "使用自定义端口和密码创建Redis服务器"
        def server = EmbeddedServerHelper.redisServer(customPort, password)

        then: "验证服务器创建成功且类型正确"
        server != null
        server instanceof EmbeddedRedisServer
    }

    /**
     * 测试使用自定义端口创建Kafka服务器
     *
     * 这个测试方法验证EmbeddedServerHelper能够使用自定义端口成功创建Kafka服务器，
     * 并检查创建的服务器是否为正确的类型。
     */
    def "test create Kafka server with custom port"() {
        given: "准备自定义端口"
        def customPort = 9093

        when: "使用自定义端口创建Kafka服务器"
        def server = EmbeddedServerHelper.kafkaServer(customPort)

        then: "验证服务器创建成功且类型正确"
        server != null
        server instanceof EmbeddedKafkaServer
    }

    /**
     * 测试使用自定义Kafka端口和Zookeeper端口创建Kafka服务器
     *
     * 这个测试方法验证EmbeddedServerHelper能够使用自定义Kafka端口和Zookeeper端口成功创建Kafka服务器，
     * 并检查创建的服务器是否为正确的类型。
     */
    def "test create Kafka server with custom Kafka port and Zookeeper port"() {
        given: "准备自定义Kafka端口和Zookeeper端口"
        def kafkaPort = 9093
        def zookeeperPort = 2182

        when: "使用自定义Kafka端口和Zookeeper端口创建Kafka服务器"
        def server = EmbeddedServerHelper.kafkaServer(kafkaPort, zookeeperPort)

        then: "验证服务器创建成功且类型正确"
        server != null
        server instanceof EmbeddedKafkaServer
    }

    /**
     * 测试使用扩展机制创建嵌入式服务器
     *
     * 这个测试方法验证EmbeddedServerHelper能够使用扩展机制（通过服务器类型名称和端口）
     * 成功创建嵌入式服务器，并检查创建的服务器是否为正确的类型。
     */
    def "test embedded server using extension mechanism"() {
        when: "使用扩展机制创建Redis服务器"
        def server = EmbeddedServerHelper.embeddedServer("redis", 6380)

        then: "验证服务器创建成功且类型正确"
        server != null
        server instanceof EmbeddedServer
    }

    /**
     * 测试私有构造函数抛出异常
     *
     * 这个测试方法验证EmbeddedServerHelper的私有构造函数在被调用时会抛出异常，
     * 确保该工具类不能被实例化。
     */
    def "test private constructor throws exception"() {
        when: "尝试通过反射调用私有构造函数"
        def constructor = EmbeddedServerHelper.class.getDeclaredConstructor()
        constructor.setAccessible(true)
        constructor.newInstance()

        then: "验证抛出的异常包含预期的错误信息"
        def exception = thrown(Exception)
        exception.cause.message.contains("should not be instantiated")
    }
}
