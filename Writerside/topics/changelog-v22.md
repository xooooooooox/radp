# v22

### Dependencies

- Upgrade `io.gatling.highcharts:gatling-charts-highcharts` from `3.2.1` to `3.10.0`.
- Upgrade `io.github.git-commit-id:git-commit-id-maven-plugin` from `9.0.1` to `9.0.2`.
- Remove unused MongoDB dependencies.

### Build

- Upgrade `io.gatling:gatling-maven-plugin` from `3.0.3` to `4.6.0`.
- Remove unused `disableCompiler` configuration in the Gatling plugin.
- Update Checkstyle config location.
- Add default `pluginGroup` to `.mvn/settings.xml`.
- Optimize profile `code-review`:
  - Add `sonar.login`.
  - Add `sonar.qualitygate.wait`.
- Optimize profile `unit-test`.
- Rename profile `aggregate-reports` to `site-aggregate`.
- Remove unused `argLine` configuration in Surefire to fix Jacoco integration.
- Add profile `o-wrapper`.
- Remove property `sonar.login` to avoid deprecation warnings.
- Change `maven-release-plugin` `tagNameFormat` from `x.y.z` to `vx.y.z`.
- Add `skipViaCommandLine` option to override the `git-commit-id-maven-plugin` skip flag.

### Scaffold

- Avoid generating incorrect `.gitlab-ci.yml`.
- Update `archetype-catalog-vcs.xml`.
- Update IDEA and Checkstyle configuration.
- Update `.editorconfig`.
- Update `application-local.yaml` to use dynamic ports.
- Update `archetype-metadata.xml`:
  - Include `.coding` directory.
  - Include `.editorconfig`.
- Update `archtype-metadata.xml` to include `.devcontainer`.
- Update `.mvn/settings.xml`:
  - Add default `pluginGroup`.
  - Replace `devops.release.arguments` with `maven.release.arguments`.
- Update profiles in `.mvn/maven.config` to include `code-review`.
- Update `.gitlab-ci.yml`.
- Optimize `checkstyle-suppressions.xml`:
  - Add a suppression for `target/generated-sources`.
- Optimize `assembly.xml` to include `jib-maven.tar`.
- Fix Writerside configuration in the scaffold.

### Style

- Update code style and Checkstyle configuration.
- Optimize import order.

