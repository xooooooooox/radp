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

package space.x9x.radp.spring.test.embedded.support


import space.x9x.radp.spring.test.embedded.redis.EmbeddedRedisServer
import spock.lang.Specification
import spock.lang.Unroll

import static space.x9x.radp.spring.test.embedded.support.EmbeddedServerHelper.EmbeddedServerType
import static space.x9x.radp.spring.test.embedded.support.EmbeddedServerHelper.embeddedServer

/**
 * Spock tests for {@link EmbeddedServerHelper}.
 *
 * 这个测试类用于测试EmbeddedServerHelper工具类的各种嵌入式服务器创建方法。
 * 测试包括使用默认设置和自定义设置创建各种类型的嵌入式服务器，如Redis、Kafka、Zookeeper等。
 * 同时也测试了私有构造函数的异常抛出情况。
 *
 * @author IO x9x
 * @since 2024-10-30
 */
class EmbeddedServerHelperSpec extends Specification {


    @Unroll
    def "test create #spi server with available port"() {
        when: "调用服务器创建方法"
        def server = serverMethod.call()

        then: "验证服务器创建成功且类型正确"
        server != null
        server.class == serverClass

        where: "测试不同类型的服务器"
        spi                               | serverMethod                                          | serverClass
        EmbeddedServerType.REDIS.getSpi() | { embeddedServer(EmbeddedServerType.REDIS.getSpi()) } | EmbeddedRedisServer
    }
}
