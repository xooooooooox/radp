package space.x9x.radp.spring.test.container.support

import org.testcontainers.utility.DockerImageName
import spock.lang.Specification
import spock.lang.Unroll

import java.time.Duration

/**
 * Spock tests for {@link ContainerHelper}.
 *
 * 这个测试类用于测试ContainerHelper工具类的各种容器创建方法。
 * 测试包括使用默认设置和自定义设置创建各种类型的容器，如Redis、MySQL、Zookeeper等。
 * 同时也测试了私有构造函数的异常抛出情况。
 *
 * @author x9x
 * @since 2024-10-30
 */
class ContainerHelperSpec extends Specification {

    /**
     * 测试使用默认设置创建各种类型的容器
     *
     * 这个测试方法验证ContainerHelper能够使用默认设置成功创建不同类型的容器，
     * 并检查创建的容器是否包含正确的Docker镜像名称。
     */
    @Unroll
    def "test create #containerType container with default settings"() {
        when: "调用容器创建方法"
        def container = containerMethod.call()

        then: "验证容器创建成功且包含正确的镜像名称"
        container != null
        container.getDockerImageName().toString().contains(imageName)

        where: "测试不同类型的容器"
        containerType   | containerMethod                              | imageName
        "Redis"         | { ContainerHelper.redisContainer() }         | "redis"
        "MySQL8"        | { ContainerHelper.mysql8Container() }        | "mysql"
        "Zookeeper"     | { ContainerHelper.zookeeperContainer() }     | "zookeeper"
        "Kafka"         | { ContainerHelper.kafkaContainer() }         | "kafka"
        "Elasticsearch" | { ContainerHelper.elasticsearchContainer() } | "elasticsearch"
        "MongoDB"       | { ContainerHelper.mongoDBContainer() }       | "mongo"
        "Nginx"         | { ContainerHelper.nginxContainer() }         | "nginx"
        "MariaDB"       | { ContainerHelper.mariaDBContainer() }       | "mariadb"
    }

    /**
     * 测试使用自定义镜像创建Redis容器
     *
     * 这个测试方法验证ContainerHelper能够使用自定义镜像成功创建Redis容器，
     * 并检查创建的容器是否使用了指定的Docker镜像。
     */
    def "test create Redis container with custom image"() {
        given: "准备自定义镜像名称"
        def customImage = "redis:7.0"

        when: "使用自定义镜像创建Redis容器"
        def container = ContainerHelper.redisContainer(customImage)

        then: "验证容器创建成功且使用了指定的镜像"
        container != null
        container.getDockerImageName().toString() == customImage
    }

    /**
     * 测试使用自定义镜像创建MySQL8容器
     *
     * 这个测试方法验证ContainerHelper能够使用自定义镜像成功创建MySQL8容器，
     * 并检查创建的容器是否使用了指定的Docker镜像。
     */
    def "test create MySQL8 container with custom image"() {
        given: "准备自定义镜像名称"
        def customImage = "mysql:8.0.33"

        when: "使用自定义镜像创建MySQL8容器"
        def container = ContainerHelper.mysql8Container(customImage)

        then: "验证容器创建成功且使用了指定的镜像"
        container != null
        container.getDockerImageName().toString() == customImage
    }

    /**
     * 测试使用自定义镜像和超时时间创建MySQL8容器
     *
     * 这个测试方法验证ContainerHelper能够使用自定义镜像和超时时间成功创建MySQL8容器，
     * 并检查创建的容器是否使用了指定的Docker镜像。
     */
    def "test create MySQL8 container with custom image and timeout"() {
        given: "准备自定义镜像名称和超时时间"
        def imageName = DockerImageName.parse("mysql:8.0.33")
        def timeout = Duration.ofSeconds(120)

        when: "使用自定义镜像和超时时间创建MySQL8容器"
        def container = ContainerHelper.mysql8Container(imageName, timeout)

        then: "验证容器创建成功且使用了指定的镜像"
        container != null
        container.getDockerImageName().toString() == imageName.toString()
    }

    /**
     * 测试使用自定义版本创建Zookeeper容器
     *
     * 这个测试方法验证ContainerHelper能够使用自定义版本成功创建Zookeeper容器，
     * 并检查创建的容器镜像名称是否包含指定的版本号。
     */
    def "test create Zookeeper container with custom version"() {
        given: "准备自定义版本号"
        def customVersion = "3.7.1"

        when: "使用自定义版本创建Zookeeper容器"
        def container = ContainerHelper.zookeeperContainer(customVersion)

        then: "验证容器创建成功且镜像名称包含指定的版本号"
        container != null
        container.getDockerImageName().toString().contains(customVersion)
    }

    /**
     * 测试使用自定义版本创建Kafka容器
     *
     * 这个测试方法验证ContainerHelper能够使用自定义版本成功创建Kafka容器，
     * 并检查创建的容器镜像名称是否包含指定的版本号。
     */
    def "test create Kafka container with custom version"() {
        given: "准备自定义版本号"
        def customVersion = "7.3.0"

        when: "使用自定义版本创建Kafka容器"
        def container = ContainerHelper.kafkaContainer(customVersion)

        then: "验证容器创建成功且镜像名称包含指定的版本号"
        container != null
        container.getDockerImageName().toString().contains(customVersion)
    }

    /**
     * 测试使用自定义镜像创建Elasticsearch容器
     *
     * 这个测试方法验证ContainerHelper能够使用自定义镜像成功创建Elasticsearch容器，
     * 并检查创建的容器是否使用了指定的Docker镜像。
     */
    def "test create Elasticsearch container with custom image"() {
        given: "准备自定义镜像名称"
        def customImage = "docker.elastic.co/elasticsearch/elasticsearch:8.6.0"

        when: "使用自定义镜像创建Elasticsearch容器"
        def container = ContainerHelper.elasticsearchContainer(customImage)

        then: "验证容器创建成功且使用了指定的镜像"
        container != null
        container.getDockerImageName().toString() == customImage
    }

    /**
     * 测试使用自定义镜像和超时时间创建Elasticsearch容器
     *
     * 这个测试方法验证ContainerHelper能够使用自定义镜像和超时时间成功创建Elasticsearch容器，
     * 并检查创建的容器是否使用了指定的Docker镜像。
     */
    def "test create Elasticsearch container with custom image and timeout"() {
        given: "准备自定义镜像名称和超时时间"
        def imageName = DockerImageName.parse("docker.elastic.co/elasticsearch/elasticsearch:8.6.0")
        def timeout = Duration.ofSeconds(180)

        when: "使用自定义镜像和超时时间创建Elasticsearch容器"
        def container = ContainerHelper.elasticsearchContainer(imageName, timeout)

        then: "验证容器创建成功且使用了指定的镜像"
        container != null
        container.getDockerImageName().toString() == imageName.toString()
    }

    /**
     * 测试使用自定义镜像创建MongoDB容器
     *
     * 这个测试方法验证ContainerHelper能够使用自定义镜像成功创建MongoDB容器，
     * 并检查创建的容器是否使用了指定的Docker镜像。
     */
    def "test create MongoDB container with custom image"() {
        given: "准备自定义镜像名称"
        def customImage = "mongo:6.0.0"

        when: "使用自定义镜像创建MongoDB容器"
        def container = ContainerHelper.mongoDBContainer(customImage)

        then: "验证容器创建成功且使用了指定的镜像"
        container != null
        container.getDockerImageName().toString() == customImage
    }

    /**
     * 测试使用自定义镜像和超时时间创建MongoDB容器
     *
     * 这个测试方法验证ContainerHelper能够使用自定义镜像和超时时间成功创建MongoDB容器，
     * 并检查创建的容器是否使用了指定的Docker镜像。
     */
    def "test create MongoDB container with custom image and timeout"() {
        given: "准备自定义镜像名称和超时时间"
        def imageName = DockerImageName.parse("mongo:6.0.0")
        def timeout = Duration.ofSeconds(90)

        when: "使用自定义镜像和超时时间创建MongoDB容器"
        def container = ContainerHelper.mongoDBContainer(imageName, timeout)

        then: "验证容器创建成功且使用了指定的镜像"
        container != null
        container.getDockerImageName().toString() == imageName.toString()
    }

    /**
     * 测试使用自定义镜像创建Nginx容器
     *
     * 这个测试方法验证ContainerHelper能够使用自定义镜像成功创建Nginx容器，
     * 并检查创建的容器是否使用了指定的Docker镜像。
     */
    def "test create Nginx container with custom image"() {
        given: "准备自定义镜像名称"
        def customImage = "nginx:1.24.0"

        when: "使用自定义镜像创建Nginx容器"
        def container = ContainerHelper.nginxContainer(customImage)

        then: "验证容器创建成功且使用了指定的镜像"
        container != null
        container.getDockerImageName().toString() == customImage
    }

    /**
     * 测试使用自定义镜像和超时时间创建Nginx容器
     *
     * 这个测试方法验证ContainerHelper能够使用自定义镜像和超时时间成功创建Nginx容器，
     * 并检查创建的容器是否使用了指定的Docker镜像。
     */
    def "test create Nginx container with custom image and timeout"() {
        given: "准备自定义镜像名称和超时时间"
        def imageName = DockerImageName.parse("nginx:1.24.0")
        def timeout = Duration.ofSeconds(45)

        when: "使用自定义镜像和超时时间创建Nginx容器"
        def container = ContainerHelper.nginxContainer(imageName, timeout)

        then: "验证容器创建成功且使用了指定的镜像"
        container != null
        container.getDockerImageName().toString() == imageName.toString()
    }

    /**
     * 测试使用自定义镜像创建MariaDB容器
     *
     * 这个测试方法验证ContainerHelper能够使用自定义镜像成功创建MariaDB容器，
     * 并检查创建的容器是否使用了指定的Docker镜像。
     */
    def "test create MariaDB container with custom image"() {
        given: "准备自定义镜像名称"
        def customImage = "mariadb:11.0.0"

        when: "使用自定义镜像创建MariaDB容器"
        def container = ContainerHelper.mariaDBContainer(customImage)

        then: "验证容器创建成功且使用了指定的镜像"
        container != null
        container.getDockerImageName().toString() == customImage
    }

    /**
     * 测试使用自定义镜像和超时时间创建MariaDB容器
     *
     * 这个测试方法验证ContainerHelper能够使用自定义镜像和超时时间成功创建MariaDB容器，
     * 并检查创建的容器是否使用了指定的Docker镜像。
     */
    def "test create MariaDB container with custom image and timeout"() {
        given: "准备自定义镜像名称和超时时间"
        def imageName = DockerImageName.parse("mariadb:11.0.0")
        def timeout = Duration.ofSeconds(90)

        when: "使用自定义镜像和超时时间创建MariaDB容器"
        def container = ContainerHelper.mariaDBContainer(imageName, timeout)

        then: "验证容器创建成功且使用了指定的镜像"
        container != null
        container.getDockerImageName().toString() == imageName.toString()
    }

    /**
     * 测试私有构造函数抛出异常
     *
     * 这个测试方法验证ContainerHelper的私有构造函数在被调用时会抛出异常，
     * 确保该工具类不能被实例化。
     */
    def "test private constructor throws exception"() {
        when: "尝试通过反射调用私有构造函数"
        def constructor = ContainerHelper.class.getDeclaredConstructor()
        constructor.setAccessible(true)
        constructor.newInstance()

        then: "验证抛出的异常包含预期的错误信息"
        def exception = thrown(Exception)
        exception.cause.message.contains("should not be instantiated")
    }

    /**
     * 测试启动容器方法
     *
     * 这个测试方法验证ContainerHelper的startContainer方法能够成功启动容器，
     * 并且在容器已经运行的情况下不会重复启动。
     */
    def "test startContainer method"() {
        given: "准备一个Redis容器"
        def container = ContainerHelper.redisContainer()

        when: "启动容器"
        def startedContainer = ContainerHelper.startContainer(container)

        then: "验证容器已启动且返回的是同一个容器"
        startedContainer != null
        startedContainer.is(container)
        startedContainer.isRunning()

        when: "再次启动已经运行的容器"
        def reStartedContainer = ContainerHelper.startContainer(startedContainer)

        then: "验证返回的仍然是同一个容器"
        reStartedContainer.is(startedContainer)

        cleanup: "停止并关闭容器"
        if (container != null && container.isRunning()) {
            container.stop()
        }
    }

    /**
     * 测试停止容器方法
     *
     * 这个测试方法验证ContainerHelper的stopContainer方法能够成功停止正在运行的容器，
     * 并且在容器未运行的情况下不会抛出异常。
     */
    def "test stopContainer method"() {
        given: "准备并启动一个Redis容器"
        def container = ContainerHelper.redisContainer()
        ContainerHelper.startContainer(container)

        when: "停止容器"
        ContainerHelper.stopContainer(container)

        then: "验证容器已停止"
        !container.isRunning()

        when: "再次停止已经停止的容器"
        ContainerHelper.stopContainer(container)

        then: "不会抛出异常"
        !container.isRunning()

        when: "尝试停止null容器"
        ContainerHelper.stopContainer(null)

        then: "不会抛出异常"
        noExceptionThrown()
    }
}
