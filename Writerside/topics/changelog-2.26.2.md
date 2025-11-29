# 2.26.2

## Features

- Allow multiple MyBatis autofill strategies to run for the same entity so `BasePO` audit fields can coexist with custom
  logic
  (for example, tenant-specific fields).
- Add `TenantContextHolder` and `TenantAutoFillStrategy` (via `radp-solution-tenant`) to populate `tenantId`
  automatically for `TenantBasePO`.

## Scaffold

- Update scaffold default `radpVersion` to `2.26.2`.
