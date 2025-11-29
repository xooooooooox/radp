# v23

## Build

- Optimize `.mvn`:
  - Add `jvm.config`.
  - Add private registry server configuration to Maven settings.
- Optimize profiles `o-release` and `o-tar`.
- Optimize assembly:
  - Change dist filename from `${project.build.finalName}-assembly.tar.gz`
    to `${project.build.finalName}-assembly-${project.version}.tar.gz`.
  - Remove the `zip` format from assembly configuration.
- Optimize `maven-release-plugin`:
  - Add `maven.release.extraPreparationProfiles`.
  - Add `maven.release.extraReleaseProfiles`.
- Add profile `o-catalog`.
- Remove redundant `auto-release` profile.
- Optimize `jib-maven-plugin` configuration to support building images without the Docker daemon.

## Dependencies

- Remove `archetype-packaging` from `pluginManagement` to fix
  `Failed to retrieve plugin descriptor … No plugin descriptor found at META-INF/maven/plugin.xml`.
