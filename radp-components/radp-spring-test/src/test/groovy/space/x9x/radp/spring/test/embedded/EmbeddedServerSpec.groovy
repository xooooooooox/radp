package space.x9x.radp.spring.test.embedded

import space.x9x.radp.spring.test.embedded.support.EmbeddedServerHelper
import spock.lang.Specification

/**
 * 嵌入式服务器测试类
 *
 * 这个测试类用于验证不同类型的嵌入式服务器（Redis、Kafka、Zookeeper）
 * 能够正确启动和关闭。
 *
 * @author x9x
 * @since 2024-10-13 17:51
 */
class EmbeddedServerSpec extends Specification {

    /**
     * 测试嵌入式服务器的启动和关闭
     *
     * 这个测试方法验证不同类型的嵌入式服务器能够正确启动和关闭，
     * 并检查服务器的运行状态是否符合预期。
     */
    def "test embedded server startup and shutdown"() {
        given: "创建指定类型和端口的嵌入式服务器"
        IEmbeddedServer embeddedServer = EmbeddedServerHelper.embeddedServer(spi, port)

        when: "启动服务器"
        embeddedServer.startup()

        then: "验证服务器已成功启动"
        embeddedServer.isRunning() == expectedRunningState

        when: "关闭服务器"
        embeddedServer.shutdown()

        then: "验证服务器已成功关闭"
        !embeddedServer.isRunning()

        where: "测试不同类型的服务器"
        spi         | port | expectedRunningState
        "redis"     | 6379 | true
        "kafka"     | 9092 | true
        "zookeeper" | 2181 | true
    }
}
