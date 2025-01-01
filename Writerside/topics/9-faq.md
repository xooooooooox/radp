# FAQ

<show-structure for="chapter" depth="2"/>

## 常见报错

### Q1: Maven-Wrapper download maven got error 401?

```xml

<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-wrapper-plugin</artifactId>
    <version>${maven-wrapper-plugin.version}</version>
    <configuration>
        <mavenVersion>3.9.9</mavenVersion>
        <distributionType>onlyScript</distributionType>
    </configuration>
</plugin>
```

- 首次引入该插件时，通过执行该插件的 `wrapper:wrapper` goal，会在工程主目录下生成 `mvnw`、`mvnw.cmd`、`.mvn` 等文件,
  具体生成的哪些内容取决于 `distributionType`
- `.mvnw` 会根据 `.mvn/wrapper/maven-wrapper.properties` 文件中的配置，下载 maven `$MAVEN_HOME/wrapper/dists` 目录下 (如
  `~/.m2/wrapper/dists/apache-maven-3.9.8-bin`)

默认情况下，插件成成的 `maven-wrapper.properties` 配置如下

```shell
distributionUrl=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.5/apache-maven-3.9.5-bin.zip
wrapperUrl=https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar
```

所以默认情况下, 下载 maven 的 url 是 <https://repo.maven.apache.org/maven2/>, 如果该地址无法访问, 可以修改成其他地址.
但如果你使用的是私服，比如 artifactory 或者 nexus 且未开启匿名访问，那么需要修改成 artifactory 的地址，并配置用户名和密码，如下所示

```shell
distributionUrl=https://<这里输入artifory usernamme>:<artifactory password>@<your artifactory domain>/artifactory/<repo name>/org/apache/maven/apache-maven/3.9.8/apache-maven-3.9.8-bin.zip
wrapperUrl=https://<这里输入artifory usernamme>:<artifactory password>@<your_artifactory_domain>/artifactory/<repo name>/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar
```

### Q2: Fatal error compiling: java.lang.NoSuchFieldError: 'com.sun.tools.javac.tree.JCTree qualid'

#### 现象

`mvn clean install` 时报错：

```
[ERROR] Failed to execute goal org.apache.maven.plugins:maven-compiler-plugin:3.10.1:compile (default-compile) on project radp-commons: Fatal error compiling: java.lang.NoSuchFieldError: Class com.sun.tools.javac.tree.JCTree$JCImport does not have member field 'com.sun.tools.javac.tree.JCTree qualid' -> [Help 1]
```

#### 原因

lombok 版本与 JDK 不兼容导致。

#### 解决方案

当前工程使用的是 `JDK8`, SpringBoot 2.7.x, 默认使用的是 `lombok 1.18.26`，将 lombok 升级到 `1.18.30` 可解决这个报错。

参考: <https://github.com/thingsboard/thingsboard/issues/9812>

### Q3: 无法运行 Groovy 单元测试

#### 现象

在运行 `radp-extensions` 的 `ExtensionLoaderSpec` 时, 报错如下

```
Internal Error occurred.
org.junit.platform.commons.JUnitException: TestEngine with ID 'spock' failed to discover tests
	at org.junit.platform.launcher.core.EngineDiscoveryOrchestrator.discoverEngineRoot(EngineDiscoveryOrchestrator.java:160)
	at org.junit.platform.launcher.core.EngineDiscoveryOrchestrator.discoverSafely(EngineDiscoveryOrchestrator.java:134)
	at org.junit.platform.launcher.core.EngineDiscoveryOrchestrator.discover(EngineDiscoveryOrchestrator.java:108)
	at org.junit.platform.launcher.core.EngineDiscoveryOrchestrator.discover(EngineDiscoveryOrchestrator.java:80)
	at org.junit.platform.launcher.core.DefaultLauncher.discover(DefaultLauncher.java:110)
	at org.junit.platform.launcher.core.DefaultLauncher.execute(DefaultLauncher.java:86)
	at org.junit.platform.launcher.core.DefaultLauncherSession$DelegatingLauncher.execute(DefaultLauncherSession.java:86)
	at org.junit.platform.launcher.core.SessionPerRequestLauncher.execute(SessionPerRequestLauncher.java:53)
	at com.intellij.junit5.JUnit5IdeaTestRunner.startRunnerWithArgs(JUnit5IdeaTestRunner.java:57)
	at com.intellij.rt.junit.IdeaTestRunner$Repeater$1.execute(IdeaTestRunner.java:38)
	at com.intellij.rt.execution.junit.TestsRepeater.repeat(TestsRepeater.java:11)
	at com.intellij.rt.junit.IdeaTestRunner$Repeater.startRunnerWithArgs(IdeaTestRunner.java:35)
	at com.intellij.rt.junit.JUnitStarter.prepareStreamsAndStart(JUnitStarter.java:232)
	at com.intellij.rt.junit.JUnitStarter.main(JUnitStarter.java:55)
Caused by: org.junit.platform.commons.JUnitException: ClassSelector [className = 'space.x9x.radp.extension.ExtensionLoaderSpec'] resolution failed
	at org.junit.platform.launcher.listeners.discovery.AbortOnFailureLauncherDiscoveryListener.selectorProcessed(AbortOnFailureLauncherDiscoveryListener.java:39)
	at org.junit.platform.engine.support.discovery.EngineDiscoveryRequestResolution.resolveCompletely(EngineDiscoveryRequestResolution.java:102)
	at org.junit.platform.engine.support.discovery.EngineDiscoveryRequestResolution.run(EngineDiscoveryRequestResolution.java:82)
	at org.junit.platform.engine.support.discovery.EngineDiscoveryRequestResolver.resolve(EngineDiscoveryRequestResolver.java:113)
	at org.spockframework.runtime.SpockEngine.discover(SpockEngine.java:28)
	at org.junit.platform.launcher.core.EngineDiscoveryOrchestrator.discoverEngineRoot(EngineDiscoveryOrchestrator.java:152)
	... 13 more
Caused by: org.junit.platform.commons.PreconditionViolationException: Could not load class with name: space.x9x.radp.extension.ExtensionLoaderSpec
	at org.junit.platform.engine.discovery.ClassSelector.lambda$getJavaClass$0(ClassSelector.java:75)
	at org.junit.platform.commons.function.Try$Failure.getOrThrow(Try.java:335)
	at org.junit.platform.engine.discovery.ClassSelector.getJavaClass(ClassSelector.java:74)
	at org.spockframework.runtime.ClassSelectorResolver.resolve(ClassSelectorResolver.java:28)
	at org.junit.platform.engine.support.discovery.EngineDiscoveryRequestResolution.lambda$resolve$2(EngineDiscoveryRequestResolution.java:134)
	at java.base/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:212)
	at java.base/java.util.ArrayList$ArrayListSpliterator.tryAdvance(ArrayList.java:1686)
	at java.base/java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:144)
	at java.base/java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:574)
	at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:560)
	at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:546)
	at java.base/java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
	at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:265)
	at java.base/java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:662)
	at org.junit.platform.engine.support.discovery.EngineDiscoveryRequestResolution.resolve(EngineDiscoveryRequestResolution.java:185)
	at org.junit.platform.engine.support.discovery.EngineDiscoveryRequestResolution.resolve(EngineDiscoveryRequestResolution.java:125)
	at org.junit.platform.engine.support.discovery.EngineDiscoveryRequestResolution.resolveCompletely(EngineDiscoveryRequestResolution.java:91)
	... 17 more
Caused by: java.lang.ClassNotFoundException: space.x9x.radp.extension.ExtensionLoaderSpec
	at java.base/jdk.internal.loader.BuiltinClassLoader.loadClass(BuiltinClassLoader.java:641)
	at java.base/jdk.internal.loader.ClassLoaders$AppClassLoader.loadClass(ClassLoaders.java:188)
	at java.base/java.lang.ClassLoader.loadClass(ClassLoader.java:525)
	at org.junit.platform.commons.util.ReflectionUtils.lambda$tryToLoadClass$9(ReflectionUtils.java:829)
	at org.junit.platform.commons.function.Try.lambda$call$0(Try.java:57)
	at org.junit.platform.commons.function.Try.of(Try.java:93)
	at org.junit.platform.commons.function.Try.call(Try.java:57)
	at org.junit.platform.commons.util.ReflectionUtils.tryToLoadClass(ReflectionUtils.java:792)
	at org.junit.platform.commons.util.ReflectionUtils.tryToLoadClass(ReflectionUtils.java:748)
	... 32 more
```
{collapsible="true" collapsed-title="org.junit.platform.commons.JUnitException: TestEngine with ID 'spock' failed to discover tests"}

#### 原因

- 保证使用了正确版本的 `maven-surefire-plugin` 插件
- 保证 IntelliJ 使用的 JDK 版本与 pom.xml 中设定的 `<maven.compiler.source>` JDK 版本保持一致

#### 解决方案

- 对于当前工程由于使用了 JUnit5 所以需要使用 `maven-surefire-plugin` 插件的版本需要使用 3.0.X+
- 当前工程使用的是 JDK 1.8, 所以需要保证 IntelliJ 的 SDK 也需要使用对应的 JDK
- 需要 enable profile `Unit-Test`

### Q4: IntelliJ Resource Bundle 中文显示乱码问题

#### 原因

`*.properties` 文件默认使用 `ISO-8859-1` 编码，而中文字符在 `ISO-8859-1` 中不存在，所以导致乱码。

#### 解决方案

依次点击 `File` -> `Settings` -> `File Encodings` -> 开启 `Transparent native-to-ascii conversation`


### Q5: GitHub actions deploy to GitHub Pages got error "protection rules"

#### 现象

在使用 Github Actions 发布到 GitHub Pages 报错如下

```
Branch "feat/241218-devops-writerside" is not allowed to deploy to github-pages due to environment protection rules.
```

#### 原因

```yaml
deploy:
  environment:
    name: github-pages
    url: ${{ steps.deployment.outputs.page_url }}
```

因为这里的 `deploy` job 中的 `github-pages` environment 有分支保护限制, 所以这个 Job 执行时报错了.(有点类似于 Git **受保护分支** 关于 git push 的限制)

#### 解决方案

调整这个 `github-pages environment` 的 `protection rule` 或者使用 `github-pages environment` 允许的分支进行 deploy.

- Go to your repository’s Settings.
- Scroll down to Environments.
- Click on the github-pages environment.
  - Under Deployment branches, verify whether the branch `feat/241218-devops-writerside` is allowed. If not:
    - Add it explicitly, or adjust the branch protection pattern (e.g., add feat/* if you want all feature branches to be eligible).

![9.2_4.png](9.1_1.png) { width="700" }

## 编码技巧

### Q1: IntelliJ 无法快速输入 TODO 和 FIXME

![9_1_1](9.2_1.png) { width="700" }

![9.2_2](9.2_2.png) { width="700" }

### Q2: IntelliJ 如何进行多行编辑

比如有一张表的DDL如下:

```sql
CREATE TABLE rule_tree
(
    id                 BIGINT(11)  NOT NULL AUTO_INCREMENT COMMENT '自增 ID',
    tree_id            VARCHAR(32) NOT NULL COMMENT '规则树ID',
    tree_name          VARCHAR(64) NOT NULL COMMENT '规则树名称',
    tree_desc          VARCHAR(128)         DEFAULT NULL COMMENT '规则树描述',
    tree_node_rule_key VARCHAR(32) NOT NULL COMMENT '规则树根入口规则',
    created_date       TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    last_modified_date TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后修改时间',
    PRIMARY KEY (id),
    UNIQUE KEY ux_tree_id (tree_id)
) COMMENT '规则树表';
```

期望手动创建一个 `RuleTreePO`, 如何利用 IntelliJ 的功能?

- 第一步: 使用快捷键 `commmand` + `option` + `shift`, 然后用`鼠标`, 逐行选择需要的变量.(比如: 这里就是 `id`, `tree_id`,
  `tree_name` 等)
- 第二步: 粘贴到 `RuleTreePO` 中
    - ![9_1_3.png](9.2_3.png)  { width="700" }
- 第三步: 在 `RuleTreePO` 中, 使用 `option` + `鼠标`, 同时选择多行, 并将光标定位到同一列,且为最左侧
- 第四步: 将上一步的多行光标, 统一移动到所有成员变量的尾部(使用快捷键 `ctrl` + `e`)
- 第五步: 将上一步的多行光标, 统一移动到所有成员变量的头部(使用快捷键 `ctrl` + `a`)
- 第六步: 删除变量前置的空格, 使得所有变量左对齐(使用快捷键`option` + `delete`)
- 第七步: 键盘输入 `private String `, 然后 `ctrl` + `e`, 然后输入 `;`
- 第八步: 格式化代码 `command` + `option` + `L`
- 第九步: 使用 `第一步`的方法, 从 `sql` 中复制每个变量的 `comment`(它们将作为 `RuleTreePO` 的 javadoc)
- 第十步: 使用 `第三步` 的方法, 同时选中通过, 并使用 `ctrl` + `e` 将光标定位到变量尾部
- 第十一步: 输入 `回车`, 然后连续使用两次 `ctrl` + `p`, 使得这几个多行光标, 均定位在每个变量的上一行, 也就是javadoc应该在的位置
- 第十二步: 直接粘贴 `第九步` 复制的 `comment`
- 第十三步: 在上一步的基础上, 使用 `ctrl`+`a` 将光标移动到注释的左侧, 输入 `/**`, 然后 `ctrl`+`e`后,输入 `*/`
- 第十四步: 格式化代码 `command` + `option` + `L`
- 第十五步: 借助 `String Manipulation` 将变量转化为驼峰风格
- 最后: 手工调整变量数据类型

### Q3: 如何抑制 sonarlint 警告

```java
public class Holder<T> {

    /**
     * 存储的值，使用 volatile 关键字修饰以保证多线程间的可见性
     * volatile 变量不会被线程本地缓存，对 volatile 变量的读写都是直接从主内存中进行
     */
    @SuppressWarnings("java:S3077")
    private volatile T value;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
```

如上，可以通过 `@SuppressWarnings("squid:Sxxxx)` 或者 `@SuppressWarnings("java:Sxxxx)` 其中 `xxxx` 即 sonarlint rule id

### Q4: Writerside 当前 topic 目录树显示层级

默认情况下, Writerside 当前 topic 右侧目录结构, 只显示到二级标题(即 `##`), 可通过如下方式调整该默认行为.

在当前 topic 文件中, 添加 `<show-structure for="chapter" depth="2"/>`, 通过 `depth` 调整默认显示层级.

具体可参考 [Writerside Structure Elements](https://www.jetbrains.com/help/writerside/structural-elements.html#topic-navigation)