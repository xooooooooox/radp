# 从 Dockerfile 构建容器

## 如何编写 Dockerfile

Dockerfile 是一个文本文件，包含构建 Docker 镜像的指令。以下是一个基本的 Nginx Dockerfile 示例：

```dockerfile
FROM nginx:1.21.6
COPY index.html /usr/share/nginx/html/index.html
EXPOSE 80
```

这个简单的 Dockerfile 做了以下几件事：

1. 使用官方 Nginx 1.21.6 镜像作为基础
2. 复制本地的 index.html 文件到容器的 `/usr/share/nginx/html/index.html` 路径
3. 暴露 80 端口

## Dockerfile 应该放置在哪里

在 TestContainers 框架中，Dockerfile 通常放置在以下位置：

1. **测试资源目录**：通常是 `src/test/resources` 目录下
2. **专用子目录**：为了更好的组织，可以创建一个专用子目录，例如 `src/test/resources/docker/nginx/Dockerfile`

## 如何读取 Dockerfile

使用 TestContainers 的 `ImageFromDockerfile` 类可以从 Dockerfile 构建容器。以下是完整的示例代码：

```java

@Testcontainers
public class DockerfileContainerTest {

    @Container
    private GenericContainer<?> nginx = new GenericContainer<>(
            new ImageFromDockerfile("custom-nginx")
                    // 方法一：从类路径资源读取 Dockerfile
                    .withFileFromClasspath("Dockerfile", "docker/nginx/Dockerfile")
                    // 复制其他需要的文件到构建上下文
                    .withFileFromClasspath("index.html", "volumes/nginx/index.html")

            // 方法二：从文件系统路径读取 Dockerfile
            // .withFileFromPath("Dockerfile", Paths.get("src/test/resources/docker/nginx/Dockerfile"))
            // .withFileFromPath("index.html", Paths.get("src/test/resources/volumes/nginx/index.html"))
    ).withExposedPorts(80);

    @Test
    void testCustomNginxContainer() throws IOException {
        String url = String.format("http://%s:%s", nginx.getHost(), nginx.getMappedPort(80));
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        assertEquals(200, responseCode); // 验证 Nginx 返回 200 OK
    }
}
```

## 关键方法说明

1. **`new ImageFromDockerfile(String imageName)`**：创建一个新的 Docker 镜像构建器，指定镜像名称

2. **`withFileFromClasspath(String path, String resourcePath)`**：
  - `path`：文件在构建上下文中的路径
  - `resourcePath`：类路径中资源的路径

3. **`withFileFromPath(String path, Path localPath)`**：
  - `path`：文件在构建上下文中的路径
  - `localPath`：本地文件系统中文件的路径

4. **`withExposedPorts(int... ports)`**：指定容器暴露的端口

## 完整工作流程

1. 在 `src/test/resources/docker/nginx/` 目录下创建 Dockerfile
2. 在 `src/test/resources/volumes/nginx/` 目录下放置 index.html 等资源文件
3. 使用 `ImageFromDockerfile` 类构建容器
4. 使用 `withFileFromClasspath` 或 `withFileFromPath` 方法添加 Dockerfile 和其他资源文件
5. 启动容器并进行测试

这种方法允许您完全控制容器的构建过程，可以自定义镜像以满足特定的测试需求。