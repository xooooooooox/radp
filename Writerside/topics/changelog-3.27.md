# 3.27

## Bug fixes

- Fix `radp-logging-spring-boot-starter` to resolve the warning:
  `The 'condition' attribute in the <if> element is deprecated and slated for removal`.

## Dependency Management

- Reorder dependencies so that all BOMs are grouped at the top.
- Remove some entries from `dependencyManagement` and let spring-boot-dependencies handle those dependencies
  - Remove `org.springframework.kafka:spring-kafka:3.9.0`
  - Remove the property `netty.version=4.1.105.Final`
  - Remove the property `lombok.version=1.18.30`
  - Remove the property `testcontainers.version=1.21.0`
  - Remove the property `testcontainers-redis.version=2.2.2`
- Add properties
  - Add the property `swagger-api.version=2.2.29`.
- Add dependencies
  - Add dependency `io.swagger.core.v3:swagger-annotations-jakarta:2.2.29`.
- Upgrade dependency `retrofit` from `2.9` to `3.0`.
- Upgrade dependency `spring-boot` from `3.4.5` to `3.5.8`

## Plugin Management

- Remove some entries from `pluginManagement` and let spring-boot-dependencies handle those dependencies
  - Remove the property `maven-javadoc-plugin.version=3.5.0`
  - Remove the property `maven-deploy-plugin.version=3.0.0`
  - Remove the property `versions-maven-plugin.version=2.14.2`

## Scaffold

- Default `radpVersion` is `3.27`.
- `xxx-type` layer uses `swagger-annotations-jakarta`.
