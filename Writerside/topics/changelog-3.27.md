# 3.27

## Bug fixes

- Fix `radp-logging-spring-boot-starter` to resolve the warning:
  `The 'condition' attribute in the <if> element is deprecated and slated for removal`.
- Update `DruidDataSourceAutoConfigure` import: from
  `com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure` to
  `com.alibaba.druid.spring.boot3.autoconfigure.DruidDataSourceAutoConfigure`

## Dependency Management

- Reorder dependencies so that all BOMs are grouped at the top.
- Remove some entries from `dependencyManagement` and let spring-boot-dependencies handle those dependencies
  - Remove `org.springframework.kafka:spring-kafka:3.9.0`
  - Remove the property `netty.version=4.1.105.Final`
  - Remove the property `lombok.version=1.18.30`
  - Remove the property `testcontainers.version=1.21.0`
  - Remove the property `testcontainers-redis.version=2.2.2`
  - Remove the property `liquibase.version=4.31.1`
- Add properties
  - Add the property `swagger-api.version=2.2.29`.
- Add dependencies
  - Add dependency `io.swagger.core.v3:swagger-annotations-jakarta:2.2.29`.
- Upgrade dependency `retrofit` from `2.9` to `3.0`.
- Upgrade dependency `spring-boot` from `3.4.5` to `3.5.8`.
- Upgrade dependency `druid` from `1.2.16` to `1.2.27`, use `com.alibaba:druid-spring-boot-3-starter` instead of
  `com.alibaba:druid-spring-boot-starter`.
- Upgrade dependency `redisson`
  - upgrade `redisson-spring-boot-starter` from `3.32.0` to `3.52.0`
  - use `redisson-spring-data-35` instead of `redisson-spring-data-27`
- Remove dependency `spring-security-oauth2-autoconfigure`, Spring Boot 3.x BOM no longer manages
  spring-security-oauth2-autoconfigure.

## Plugin Management

- Remove some entries from `pluginManagement` and let spring-boot-dependencies handle those dependencies
  - Remove the property `maven-javadoc-plugin.version=3.5.0`
  - Remove the property `maven-deploy-plugin.version=3.0.0`
  - Remove the property `versions-maven-plugin.version=2.14.2`

## Scaffold

- Default `radpVersion` is `3.27`.
- `xxx-type` layer uses `swagger-annotations-jakarta`.
