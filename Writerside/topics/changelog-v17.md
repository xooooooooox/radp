# v17

## Shared changes

### Chore — Parent

- Optimize profile `auto-jib` by splitting it into:
  - `auto-jib-buildTar`
  - `auto-jib-dockerBuild`
    to work around `jib:buildTar` not supporting multi-platform builds.
- Add profiles:
  - `o-release`
  - `o-tar`
  - `publish-harbor`
  - `publish-artifactory`

### Chore — Scaffold

- Fix `.github/trigger-releases.yml`.
- Change generated project version from `1.0.0-SNAPSHOT` to `1.0-SNAPSHOT`.
- Optimize dev-ops application configuration.
- Optimize `.mvn/settings.xml`.

## 3.17

- Scaffold default `radpVersion` is `3.17`.

## 2.17

- Scaffold default `radpVersion` is `2.17`.
