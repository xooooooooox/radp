# radp-solution-tenant

Tenant-oriented building blocks for RADP applications. The module extends the MyBatis auto-fill infrastructure so that
entities inheriting from `TenantBasePO` automatically receive a `tenantId` computed from the current
`TenantContextHolder`.

## Usage

1. Add the dependency alongside `radp-mybatis-spring-boot-starter`:

```xml

<dependency>
	<groupId>space.x9x.radp</groupId>
	<artifactId>radp-solution-tenant</artifactId>
</dependency>
```

2. Bind the tenant id at the beginning of each request (for example, in a servlet filter or test setup):

```java
TenantContextHolder.setTenantId(0L);
try{
		mapper.

insert(new DebugAutofillPO());
		}
		finally{
		TenantContextHolder.

clear();
}
```

3. Define your entities by extending `TenantBasePO`; the built-in `TenantAutoFillStrategy` fills `tenantId` while the
   default `BasePOAutoFillStrategy` (from the MyBatis starter) keeps handling audit fields such as `createdAt` and
   `updatedAt`.

You can override the strategy bean or provide additional `AutoFillStrategy` implementations when you need custom
behavior. All matching strategies are executed in `@Order` order.
