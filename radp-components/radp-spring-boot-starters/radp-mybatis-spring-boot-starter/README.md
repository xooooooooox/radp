# radp-mybatis-spring-boot-starter

Lightweight MyBatis-Plus extensions for RADP projects.

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

If BasePO doesn’t match your needs, define your own base type and provide an AutoFillStrategy bean. Extend
`space.x9x.radp.spring.data.mybatis.autofill.AbstractAutoFillStrategy` to get typed insert/update hooks.

Example base type:

```java

@Data
public class CustomBasePO {

	@TableField(fill = FieldFill.INSERT)
	private String field1;

	@TableField(fill = FieldFill.INSERT_UPDATE)
	private String field2;

}
```

Strategy bean:

```java
public class CustomAutoFillStrategy extends AbstractAutoFillStrategy<CustomBasePO> {

	public CustomAutoFillStrategy() {
		super(CustomBasePO.class);
	}

	@Override
	protected void doInsertFill(CustomBasePO entity, MetaObject metaObject) {
		// ...
	}

	@Override
	protected void doUpdateFill(CustomBasePO entity, MetaObject metaObject) {
		// ...
	}

}
```

#### Configuration summary

- Enable/disable auto-fill:
  - `radp.mybatis-plus.extension.auto-fill.enabled=true|false`
- Optional SQL rewrite (BasePO only by default; covers createdAt/updatedAt/creator/updater):
  - `radp.mybatis-plus.extension.sql-rewrite.enabled=true|false`
  - `radp.mybatis-plus.extension.sql-rewrite.created-column-name=created_at (default)`
  - `radp.mybatis-plus.extension.sql-rewrite.last-modified-column-name=updated_at (default)`
  - `radp.mybatis-plus.extension.sql-rewrite.creator-column-name=creator (default)`
  - `radp.mybatis-plus.extension.sql-rewrite.updater-column-name=updater (default)`
