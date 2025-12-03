# 2.27

## Dependency Management

- Reorder dependencies so that all BOMs are grouped at the top.
- Remove some entries from `dependencyManagement` and let spring-boot-dependencies handle those dependencies
  - Remove the property `lombok.version=1.18.30`
- Add properties
  - Add the property `swagger-api.version=2.2.8`.
- Add dependencies
  - Add dependency `io.swagger.core.v3:swagger-annotations:2.2.8`.

## Plugin Management

- Remove some entries from `pluginManagement` and let spring-boot-dependencies handle those dependencies
  - Remove the property `maven-javadoc-plugin.version=3.5.0`
  - Remove the property `versions-maven-plugin.version=2.14.2`

## Scaffold:

- Default `radpVersion` is `2.27`.
- `xxx-type` layer uses `swagger-annotations` (non-Jakarta).
