# v18.1

## Dependencies

- Override `liquibase.version` to `4.31.1`.

## Scaffold

- Optimize `entrypoint.sh`.
- Improve Liquibase integration:
  - Fix duplicate initialization caused by filename inconsistencies in changesets.
  - Improve the `changelog-init.yaml` example.
  - Improve `migration/20241018` directory structure and multi-environment support.

