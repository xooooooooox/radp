/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package space.x9x.radp.spring.test.embedded

import space.x9x.radp.spring.test.embedded.support.EmbeddedServerHelper
import spock.lang.Specification

/**
 * 嵌入式服务器测试类
 *
 * 这个测试类用于验证不同类型的嵌入式服务器（Redis、Kafka、Zookeeper）
 * 能够正确启动和关闭。
 *
 * @author IO x9x
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
        IEmbeddedServer embeddedServer = EmbeddedServerHelper.embeddedServer(spi)

        when: "启动服务器"
        embeddedServer.startup()

        then: "验证服务器已成功启动"
        embeddedServer.isRunning() == expectedRunningState

        when: "关闭服务器"
        embeddedServer.shutdown()

        then: "验证服务器已成功关闭"
        !embeddedServer.isRunning()

        where: "测试不同类型的服务器"
        spi     | expectedRunningState
        "redis" | true
    }
}
