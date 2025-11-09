# radp-jasypt-spring-boot-starter 使用指南

本 Starter 为 Spring Boot 提供开箱即用的 Jasypt 加解密支持，默认启用国密 SM4 算法，并与社区版 jasypt-spring-boot 保持兼容。

核心特性：

- 自动装配 `jasyptStringEncryptor`，默认算法为 `SM4`（等价 `SM4/CBC/PKCS5Padding`）
- 支持 `SM4/CBC/PKCS5Padding` 与 `SM4/ECB/PKCS5Padding` 两种模式
- 当算法不以 `SM4` 开头时，自动回退为 Jasypt 标准 PBE 实现（如 `PBEWithMD5AndDES` 等）
- 提供 `JasyptUtils` 工具类，便于在本地生成 `ENC(...)` 密文
- 已内置 BouncyCastle 依赖，免去额外引入

## 1. 依赖引入

Maven：

```xml

<dependency>
	<groupId>space.x9x.radp</groupId>
	<artifactId>radp-jasypt-spring-boot-starter</artifactId>
	<version>${radp.version}</version>
</dependency>
```

## 2. 快速开始（application.yaml）

- 选择算法：`SM4`（推荐，等价 `SM4/CBC/PKCS5Padding`）或 `SM4/ECB/PKCS5Padding`
- 不要把密钥明文写入配置文件，建议用环境变量或 JVM 启动参数注入

```yaml
spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/demo?useSSL=false&serverTimezone=UTC
    username: ENC(YOUR_SM4_CIPHERTEXT_FOR_USERNAME)
    password: ENC(YOUR_SM4_CIPHERTEXT_FOR_PASSWORD)

jasypt:
  encryptor:
    # 可写 SM4（等价于 SM4/CBC/PKCS5Padding）或 SM4/ECB/PKCS5Padding
    algorithm: SM4
    # 推荐通过环境变量或启动参数注入，不要硬编码
    password: ${JASYPT_ENCRYPTOR_PASSWORD:}
```

运行时提供密钥的常见方式：

- JVM 启动参数：`-Djasypt.encryptor.password=你的密钥`
- 环境变量：`JASYPT_ENCRYPTOR_PASSWORD=你的密钥`

可选（进阶）：使用外部配置文件定义加解密参数。若在代码中采用 `ExtendJasyptProperties` 读取外部文件，则支持：

- `-Djasypt.config.file=/path/jasypt.properties` 或环境变量 `JASYPT_CONFIG_FILE`
- 文件内容示例：
  ```properties
  jasypt.encryptor.algorithm=SM4/CBC/PKCS5Padding
  jasypt.encryptor.password=你的密钥
  ```

`ExtendJasyptProperties` 的读取优先级：系统属性 > 环境变量 > 外部配置文件。

## 3. 生成 ENC(...) 密文的两种方式

请务必保证“生成密文时使用的算法/密钥”与“应用运行时配置”保持一致。

方式 A：使用工具类 `JasyptUtils`（任意临时 main/测试均可）

```java
String algorithm = "SM4/CBC/PKCS5Padding"; // 或 "SM4/ECB/PKCS5Padding"

String password = "dsaf#,jds.klfj1";      // 你的密钥

String encUser = JasyptUtils.customSM4Encrypt("db_user", algorithm, password);

String encPass = JasyptUtils.customSM4Encrypt("db_password", algorithm, password);
System.out.

println(encUser);
System.out.

println(encPass);
```

然后将打印出的 Base64 文本分别填入：

```yaml
spring:
  datasource:
    username: ENC(打印出的用户名密文)
    password: ENC(打印出的密码密文)
```

方式 B：使用 Spring 上下文提供的 `StringEncryptor`
当 `jasypt.encryptor.algorithm` 以 `SM4` 开头时，Starter 会注入 `Sm4StringEncryptor`（bean 名：`jasyptStringEncryptor`）：

```java
@Autowired
private org.jasypt.encryption.StringEncryptor encryptor;

String encUser = encryptor.encrypt("db_user");

String encPass = encryptor.encrypt("db_password");
```

同样把输出放入 `ENC(...)`。

## 4. CBC 与 ECB 的区别

- `SM4/CBC/PKCS5Padding`（推荐）：每次加密随机生成 16 字节 IV，最终密文为 `Base64(IV || CIPHERTEXT)`，抗分析能力更强。
- `SM4/ECB/PKCS5Padding`：无 IV，密文为 `Base64(CIPHERTEXT)`，对明文结构敏感，安全性相对较弱，但便于对接部分遗留场景。

## 5. 密钥派生与兼容性

- 本组件从 `password` 的 UTF-8 字节派生 128 位密钥：取前 16 字节，不足 16 字节用 0 填充。
- 运行时 `algorithm` 与加密时必须一致，否则会解密失败并抛出 `SM4 decrypt error`。
- 配置 `algorithm: SM4` 等价于 `SM4/CBC/PKCS5Padding`。
- 当 `algorithm` 不以 `SM4` 开头时，自动使用 Jasypt 的 `StandardPBEStringEncryptor`，以保持与社区版的兼容。

## 6. 完整示例与启动

- 先生成密文（示例输出，替换为你的真实值）：
  - 用户名密文：`Q2R8m2V7ZIf...`
  - 密码密文：`x6mPp9X0C0...`

- application.yaml：

```yaml
spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/demo?useSSL=false&serverTimezone=UTC
    username: ENC(Q2R8m2V7ZIf...)
    password: ENC(x6mPp9X0C0...)

jasypt:
  encryptor:
    algorithm: SM4  # 等价 SM4/CBC/PKCS5Padding
    password: ${JASYPT_ENCRYPTOR_PASSWORD:}
```

- 启动命令（两种等价方式）：
  - `java -Djasypt.encryptor.password=dsaf#,jds.klfj1 -jar app.jar`
  - `JASYPT_ENCRYPTOR_PASSWORD=dsaf#,jds.klfj1 java -jar app.jar`

## 7. 属性来源与优先级说明

- 自动装配读取 Spring Environment 中的属性，遵循 Spring 常规优先级（profile、系统属性、环境变量等）。
- 若使用 `ExtendJasyptProperties` 加载外部文件，则其内部优先级为：系统属性 > 环境变量 > 外部配置文件（通过
  `-Djasypt.config.file` 或 `JASYPT_CONFIG_FILE` 指定）。

## 8. 常见问题排查与安全建议

- 解密失败：确认 `algorithm`、`password` 与生成密文时一致；确认 `ENC(...)` 外层无多余空格/引号。
- CBC/ECB 不一致：用 ECB 密文却配置 CBC（或相反）会报错，请保持一致。
- 日志泄露：`JasyptUtils` 示例会打印明文/密文，生产环境请避免输出敏感信息到日志。
- 依赖问题：本 Starter 已引入 `bcprov-jdk15to18`，无需另行添加。
