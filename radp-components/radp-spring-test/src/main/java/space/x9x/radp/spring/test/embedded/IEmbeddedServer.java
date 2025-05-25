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

package space.x9x.radp.spring.test.embedded;

import space.x9x.radp.extension.SPI;

/**
 * @author x9x
 * @since 2024-09-23 15:03
 */
@SPI
public interface IEmbeddedServer {

    /**
     * 设置服务用户名
     *
     * @param username 服务的用户名
     * @return 当前的EmbeddedServer实例，支持链式调用
     */
    default IEmbeddedServer username(String username) {
        return this;
    }

    /**
     * 设置服务密码
     *
     * @param password 服务的密码
     * @return 当前的EmbeddedServer实例，支持链式调用
     */
    default IEmbeddedServer password(String password) {
        return this;
    }

    /**
     * 设置服务端口
     *
     * @param port 服务的端口号
     * @return 当前的EmbeddedServer实例，支持链式调用
     */
    default IEmbeddedServer port(int port) {
        return this;
    }

    /**
     * 启动嵌入式服务
     */
    void startup();

    /**
     * 关闭嵌入式服务
     */
    void shutdown();

    /**
     * 检查服务是否正在运行
     *
     * @return 如果服务器正在运行，则返回true；否则返回false
     */
    boolean isRunning();
}
