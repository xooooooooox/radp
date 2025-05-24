# TestContainer 容器复用

TestContainers 支持容器复用功能，可以大幅提高测试速度，减少资源消耗。当启用容器复用后，TestContainers 不会在测试结束后销毁容器，而是保留容器以供后续测试使用。

## 启用方式

### 1. 全局启用

在 classpath 下创建 `testcontainers.properties` 文件，添加以下配置：

```properties
testcontainers.reuse.enable=true
```

这将为所有 TestContainer 容器启用复用功能。

### 2. 单个容器启用

对于特定容器，可以通过 `.withReuse(true)` 方法显式启用复用：

```java
@Container
private GenericContainer<?> container = new GenericContainer<>("nginx:1.21.6")
        .withExposedPorts(80)
        .withReuse(true);  // 显式启用复用
```

即使全局未启用复用，此容器也会被复用。

## 容器标识

为了确保每次运行复用相同的容器，建议为容器设置唯一标识符：

```java
@Container
private GenericContainer<?> container = new GenericContainer<>("nginx:1.21.6")
        .withExposedPorts(80)
        .withLabel("reuse.UUID", "my-unique-container-id");  // 设置唯一标识符
```

如果不设置标识符，TestContainers 会自动生成，但每次运行可能不同。

## 注意事项

1. 复用容器在测试结束后不会自动停止，需要手动停止或等待 Docker 自动清理
2. 复用容器适合只读测试，如果测试会修改容器状态，可能导致后续测试失败
3. 如果容器配置发生变化，需要手动停止旧容器或更改容器标识符
4. 复用容器会占用系统资源，需要注意资源管理

## 示例代码

完整示例请参考 [ContainerReuseTest.java](ContainerReuseTest.java)

```java
@Testcontainers
class ContainerReuseTest {

    // 全局复用配置
    @Container
    private GenericContainer<?> globalReuseContainer = new GenericContainer<>("nginx:1.21.6")
            .withExposedPorts(80)
            .withLabel("reuse.UUID", "global-reuse-nginx");

    // 单个容器复用配置
    @Container
    private GenericContainer<?> explicitReuseContainer = new GenericContainer<>("nginx:1.21.6")
            .withExposedPorts(80)
            .withLabel("reuse.UUID", "explicit-reuse-nginx")
            .withReuse(true);

    @Test
    void testContainer() throws Exception {
        // 容器已经启动并可以使用
        String url = String.format("http://%s:%s", globalReuseContainer.getHost(), globalReuseContainer.getMappedPort(80));
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        assertEquals(200, connection.getResponseCode());
    }
}
```
