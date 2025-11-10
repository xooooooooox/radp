# radp-mybatis-spring-boot-starter

Lightweight MyBatis-Plus extensions for RADP projects: plug-in auto-fill for audit fields and optional SQL rewrite for
BasePO audit columns (createdAt, updatedAt, creator, updater).

- Strategy-based auto-fill: you decide how to fill fields per base entity class by providing an AutoFillStrategy bean.
- Sensible defaults: built-in BasePOAutoFillStrategy supports BasePO createdAt/updatedAt/creator/updater out of the box.
- Optional SQL rewrite: when your DB uses different physical column names (e.g., created_date/last_modified_date),
  enable a small interceptor to rewrite SQL at runtime so queries and DML work without changing Java code.

## Installation

Add the starter to your project (Maven):

```xml

<dependency>
	<groupId>space.x9x.radp</groupId>
	<artifactId>radp-mybatis-spring-boot-starter</artifactId>
	<version>${radp.version}</version>
</dependency>
```

## Getting started

### Autofill

#### Enable auto-fill

```yaml
radp:
  mybatis-plus:
    extension:
      auto-fill:
        enabled: true
```

That’s it for the basic setup. The starter registers a StrategyDelegatingMetaObjectHandler and a default
BasePOAutoFillStrategy.

#### Usage scenarios

##### Scenario 1 — Use BasePO and default DB columns (created_at, updated_at)

- Entities extend space.x9x.radp.spring.data.mybatis.autofill.BasePO.
- DB columns follow MyBatis-Plus’ default snake case mapping (created_at, updated_at, creator, updater).
- Config:

```yaml
radp:
  mybatis-plus:
    extension:
      auto-fill:
        enabled: true
```

##### Scenario 2 — Use BasePO but DB column names differ (e.g., created_date, last_modified_date)

When only the physical column names differ and you’d like to keep Java properties unchanged:

- Enable the SQL rewrite interceptor and set real column names.

```yaml
radp:
  mybatis-plus:
    extension:
      auto-fill:
        enabled: true
      sql-rewrite:
        enabled: true
        created-column-name: created_date
        last-modified-column-name: last_modified_date
        # Optional, if your DB uses different names for these too
        # creator-column-name: created_by
        # updater-column-name: updated_by
```

Notes:

- Rewrite applies only to statements involving BasePO entities. It does not affect CustomPO or other entities.
- The interceptor rewrites created_at/updated_at/creator/updater in generated SQL to your configured names and adds
  aliases in SELECT so mapping still works.
- For complex custom SQL or other renamed columns, consider annotating fields with @TableField(value = "...") in your
  entities.

##### Scenario 3 — Custom base class and custom fill logic

If BasePO doesn’t match your needs, define your own base type and provide an AutoFillStrategy bean.

Example base type:

```java
public class AuditFields {
	private LocalDateTime createdOn;

	private LocalDateTime modifiedOn;

	private String createdBy;

	private String modifiedBy;
	// getters/setters
}
```

Strategy bean:

```java

@Bean
public AutoFillStrategy auditStrategy() {
	return new AutoFillStrategy() {
		public boolean supports(Object entity) {
			return entity instanceof AuditFields;
		}

		public void insertFill(Object entity, org.apache.ibatis.reflection.MetaObject meta) {
			LocalDateTime now = LocalDateTime.now();
			meta.setValue("createdOn", now);
			meta.setValue("modifiedOn", now);
			String uid = currentUserId(); // implement your own context lookup
			if (uid != null) {
				meta.setValue("createdBy", uid);
				meta.setValue("modifiedBy", uid);
			}
		}

		public void updateFill(Object entity, MetaObject meta) {
			meta.setValue("modifiedOn", LocalDateTime.now());
			String uid = currentUserId();
			if (uid != null) meta.setValue("modifiedBy", uid);
		}

		private String currentUserId() {
			return "1";
		}
	};
}
```

#### Configuration summary

- Enable/disable auto-fill:
  - `radp.mybatis-plus.extension.auto-fill.enabled=true|false`
- Optional SQL rewrite (BasePO only by default; covers createdAt/updatedAt/creator/updater):
  - `radp.mybatis-plus.extension.sql-rewrite.enabled=true|false`
  - `radp.mybatis-plus.extension.sql-rewrite.scope=BASEPO|GLOBAL` (default: BASEPO). BASEPO limits rewrite to statements
    clearly involving BasePO. GLOBAL applies to all statements.
  - `radp.mybatis-plus.extension.sql-rewrite.created-column-name=created_at (default)`
  - `radp.mybatis-plus.extension.sql-rewrite.last-modified-column-name=updated_at (default)`
  - `radp.mybatis-plus.extension.sql-rewrite.creator-column-name=creator (default)`
  - `radp.mybatis-plus.extension.sql-rewrite.updater-column-name=updater (default)`

#### Notes

- No more profile-based or target-class configuration. Filling behavior is defined by AutoFillStrategy implementations.
  The starter provides a default strategy for BasePO.
- If you don’t extend BasePO and don’t register your own strategy, the handler won’t fill anything.
- For column name mismatches beyond created/updated, prefer explicit `@TableField` mappings or custom SQL.

#### Troubleshooting

- Unknown column errors on SELECT: if you use BasePO and your DB columns aren’t created_at/updated_at, enable
  sql-rewrite with your real column names, or annotate fields with @TableField(value="...").
- Strategy not invoked: ensure `radp.mybatis-plus.extension.auto-fill.enabled=true` and that your
  `AutoFillStrategy#supports` returns true for the entity.
- Multiple strategies: order them with `@Order`; the delegating handler selects the first supporting strategy.
