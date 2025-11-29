# 3.27

- DependencyManagement:
  - Add `io.swagger.core.v3:swagger-annotations-jakarta:2.2.29`.
  - Set `swagger-api.version=2.2.29`.
  - Upgrade `retrofit.version` from `2.9` to `3.0`.
  - Upgrade `spring-boot.version` from `3.4.5` to `3.5.8`
  - Remove `org.springframework.kafka:spring-kafka` and let it be managed by the Spring Boot BOM.
  - Reorder dependencies so that all BOMs are grouped at the top.
  - Remove property`netty.version`.
- Scaffold:
  - Default `radpVersion` is `3.27`.
  - `xxx-type` layer uses `swagger-annotations-jakarta`.
