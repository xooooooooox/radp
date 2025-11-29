# 4.27

## Breaking changes

- Remove module `radp-undertow-spring-boot-starter`.

## Dependency Management

- Upgrade dependency SpringBoot from `3.5.8` to `4.0.0`
- Upgrade dependency SpringCloud from `2024.0.2` to `2025.1.0`

## Tests

- Fix `ElasticsearchKibanaTest`. Use the new `ElasticsearchClient.of(...)` builder to resolve the compilation error:
  - `package org.apache.http does not exist`
  - `package org.elasticsearch.client does not exist`

## Refactor

> Resolve the compilation error

- Refactor `RadpDruidDataSourceMetricsAutoconfiguration`:
  - Directly use `javax.sql.DataSource#unwrpa(..) instead of `
    org.springframework.boot.jdbc.DataSourceUnwrapper#unwrap(..)`
  - Add dependency `spring-boot-jdbc` to resolve cannot find symbox
    `org.springframework.boot.jdbc.metadata.DataSourcePoolMetadataProvider`
- Refactor `JwtWebSecurityAutoConfiguration`: use
  `org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration` instead of
  `org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration`
- Refactor `MybatisPageHelperAutoConfiguration`: use
  `org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration` instead of
  `org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration`
- Refactor `MybatisPluginAutoConfiguration`: use
  `org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration` instead of
  `org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration`
- Refactor `CustomRedisTemplateAutoConfiguration`:
  - Add dependency `spring-boot-data-redis`
  - use `org.springframework.boot.data.redis.autoconfigure.DataRedisAutoConfiguration` instead of
    `org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration`
- Refactor `RadpRedisCacheAutoConfiguration`: use `org.springframework.boot.cache.autoconfigure.CacheProperties`
  instead of `org.springframework.boot.autoconfigure.cache.CacheProperties`

> Resolve deprecated api

- Refactor `JacksonUtils`, `DefaultXmlMapper`, `DefaultObjectMapper`:
  - Use `#setDefaultPropertyInclusion()` instead of `#setSerializationInclusion()`
  - Use `#readValue(InputStream, Class<T>)` instead of `#readValue(URL,Class<T>)`
