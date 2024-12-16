package com.x9x.radp.spring.test.embedded;

import com.x9x.radp.extension.SPI;

/**
 * @author x9x
 * @since 2024-09-23 15:03
 */
@SPI
public interface EmbeddedServer {

    /**
     * 设置服务用户名
     *
     * @param username 服务的用户名
     * @return 当前的EmbeddedServer实例，支持链式调用
     */
    default EmbeddedServer username(String username) {
        return this;
    }

    /**
     * 设置服务密码
     *
     * @param password 服务的密码
     * @return 当前的EmbeddedServer实例，支持链式调用
     */
    default EmbeddedServer password(String password) {
        return this;
    }

    /**
     * 设置服务端口
     *
     * @param port 服务的端口号
     * @return 当前的EmbeddedServer实例，支持链式调用
     */
    default EmbeddedServer port(int port) {
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
