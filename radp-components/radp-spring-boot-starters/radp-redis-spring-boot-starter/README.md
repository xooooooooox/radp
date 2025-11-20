# Redis Key Management System

[![Maven Central Version](https://img.shields.io/maven-central/v/space.x9x.radp/radp-logging-spring-boot-starter?style=for-the-badge)](https://central.sonatype.com/artifact/space.x9x.radp/radp-logging-spring-boot-starter)

This document describes the Redis key management system in the RADP Redis Spring Boot Starter, including recent
optimizations and enhancements.

## Overview

The Redis key management system provides a standardized approach to creating, validating, and working with Redis keys
across different modules of the application. It ensures that keys follow a consistent format and provides utilities for
key generation, validation, and extraction.

## Key Format

Redis keys in the RADP system follow the format:

```
application:module:entity:identifier
```

For example:

```
radp:user:profile:123
```

This format provides a clear namespace hierarchy and makes keys more readable and maintainable.

## Components

### RedisKeyConstants

The `RedisKeyConstants` class provides constants and utility methods for working with Redis keys:

- `DELIMITER`: The standard delimiter used to separate parts of Redis keys (`:`)
- `RADP_PREFIX`: The default prefix for all RADP Redis keys (`radp`)
- `DEFAULT_PROVIDER`: A default implementation of `RedisKeyProvider` for core RADP functionality

Key utility methods:

- `buildRedisKeyWithPrefix(prefix, parts...)`: Builds a Redis key with a specific prefix and additional parts
- `buildRedisKeyWithDefaultPrefix(parts...)`: Creates a namespaced Redis key using the DEFAULT_PROVIDER's prefix
- `isValidKey(key)`: Validates that a Redis key follows the standard format
- `extractPrefix(key)`: Extracts the prefix (first part) from a Redis key

### IRedisKeyProvider

The `IRedisKeyProvider` interface allows different modules to define their own Redis key formats while maintaining a
consistent approach to key generation:

```java
public interface IRedisKeyProvider {
    String getPrefix();

    default String buildKey(String... parts) {
        return RedisKeyConstants.buildRedisKeyWithPrefix(getPrefix(), parts);
    }

    // Additional utility methods...
}
```

Key utility methods:

- `buildEntityKey(entity, id)`: Builds a key for an entity with an identifier
- `buildFeatureKey(feature)`: Builds a key for a specific feature or operation
- `isKeyInNamespace(key)`: Checks if a key belongs to this provider's namespace
- `extractEntity(key)`: Extracts the entity part from a key in this provider's namespace
- `extractId(key)`: Extracts the identifier part from a key in this provider's namespace

## Implementation Example

To implement a module-specific Redis key provider:

```java
public enum MyModuleKeys implements RedisKeyProvider {
    INSTANCE;

    private static final String PREFIX = "radp:mymodule";

    // Module-specific key constants
    public static final String USER_PROFILE = INSTANCE.buildKey("user:profile");
    public static final String SESSION_DATA = INSTANCE.buildKey("session");

    @Override
    public String getPrefix() {
        return PREFIX;
    }

    // Additional module-specific methods...
}
```

## Recent Optimizations

The Redis key management system has been enhanced with the following optimizations:

1. **Improved Key Validation**: Added robust validation to ensure keys follow the standardized format
2. **Enhanced Key Generation**: Improved the performance and robustness of key generation methods
3. **Flexible Entity and ID Extraction**: Enhanced extraction methods to work with different prefix formats
4. **Additional Utility Methods**: Added convenience methods for common key operations

## Testing

The Redis key management system is thoroughly tested in the `RedisKeyManagementTest` class, which verifies:

- Key validation
- Prefix extraction
- Key building
- RedisKeyProvider functionality

## Best Practices

1. **Use RedisKeyProvider**: Implement the `RedisKeyProvider` interface for each module that needs to work with Redis
   keys
2. **Define Constants**: Define constants for commonly used keys to avoid duplication and ensure consistency
3. **Validate Keys**: Use the validation methods to ensure keys follow the standardized format
4. **Use Extraction Methods**: Use the extraction methods to parse keys and extract relevant parts

## License

[Apache 2.0 License—](../../../LICENSE)Copyright © 2024 xooooooooox
and [contributors](https://github.com/xooooooooox/radp/graphs/contributors)
