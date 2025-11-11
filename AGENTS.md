# Repository Guidelines

## Project Structure & Module Organization

RADP is a multi-module Maven build rooted at `pom.xml`. `radp-components` hosts the dependency BOM, parent POM, commons
utilities, Spring extensions, and starters (check suffixes such as `radp-spring-boot-starters` or `radp-spring-cloud`).
`radp-archetypes` contains the Maven scaffolds, `radp-plugins` keeps custom build plugins, and `radp-tests` aggregates
shared fixtures and integration stubs. Generated distributions appear in `dist/`, while the usual Maven outputs stay
under each module’s `target/`.

## Build, Test, and Development Commands

Use the wrapper to keep the pinned toolchain:

```bash
./mvnw clean verify # full build and aggregate checks
./mvnw -pl radp-components -am install # iterate on core components only
./mvnw -pl radp-archetypes/scaffold-std package # validate a single archetype
```

Combine `-pl` (project list) with `-am` (also make) whenever you need to scope work to one module without rebuilding the
whole tree.

## Coding Style & Naming Conventions

Java code follows `.coding/checkstyle/checkstyle.xml`, which extends Spring Java Format: four-space indentation,
120-character column limit, newline-brace style, and ordered imports. Package names remain lower-case dotted, artifacts
stay prefixed with `radp-`, configuration classes end with `Config`, and auto-configuration lives under
`space.x9x.radp.autoconfigure`. Run `./mvnw checkstyle:check` locally before opening a pull request; CI enforces the
same rules.

## Testing Guidelines

JUnit Jupiter and Mockito are the primary frameworks (see `radp-components/radp-parent/pom.xml`), with Cucumber hooks
available for BDD flows. Co-locate tests beside sources using the `*Tests` or `*IT` suffix, and lean on helpers under
`radp-tests` for shared fixtures. Execute `./mvnw test` for the default suite,
`./mvnw -pl radp-components/radp-spring-test verify` when touching Spring testing utilities, and include coverage or log
snippets in PRs for non-trivial changes.

## Commit & Pull Request Guidelines

History shows a Conventional Commits pattern (`feat(autofill): refactor autofill`). Keep subjects imperative, under 72
characters, and scope by module (e.g., `docs(junie)`, `fix(spring-cloud)`). Each PR should summarize the change, list
affected modules, link issues, and attach screenshots or CLI transcripts for plugin/agent work. Note any new
configuration keys or environment variables so downstream adopters can follow along.

## Security & Configuration Tips

Do not commit secrets—agent connectors and archetypes read credentials from environment variables or
`application-local.yaml`, which must stay untracked. Review generated archives in `dist/` to ensure client-specific data
is scrubbed, and double-check plugin defaults so that sensitive endpoints or tokens never ship with placeholder values
enabled.
