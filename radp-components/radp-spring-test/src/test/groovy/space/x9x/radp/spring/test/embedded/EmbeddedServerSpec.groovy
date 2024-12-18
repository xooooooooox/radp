package space.x9x.radp.spring.test.embedded

import space.x9x.radp.spring.test.embedded.support.EmbeddedServerHelper
import spock.lang.Specification

/**
 * @author x9x
 * @since 2024-10-13 17:51
 */
class EmbeddedServerSpec extends Specification {

    def "test embedded server running"() {
        expect:
        EmbeddedServer embeddedServer = EmbeddedServerHelper.embeddedServer(spi, port)
        embeddedServer.startup()
        isRunning == embeddedServer.isRunning()
        embeddedServer.shutdown()

        where:
        spi         || port || isRunning
        "redis"     || 6379 || true
        "kafka"     || 9092 || true
        "zookeeper" || 2181 || true
    }
}
