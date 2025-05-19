# Redis Key Management Pattern

This document explains the Redis key management pattern implemented in the `radp-redis-spring-boot-starter` module. The
pattern provides a standardized way to define, organize, and use Redis keys across different modules and applications.

## Overview

The Redis key management pattern consists of:

1. A `RedisKeyProvider` interface that defines the contract for Redis key providers
2. A centralized `RedisKeyConstants` utility class with common methods and a default provider
3. Module-specific implementations of `RedisKeyProvider` for custom key management

## Using the Pattern

### Basic Usage with RedisKeyConstants

For simple cases, you can use the `RedisKeyConstants` utility class directly:

```java
// Build a key with multiple parts
String key = RedisKeyConstants.buildRedisKey("radp", "user", "profile", "123");
// Result: "radp:user:profile:123"

// Create a namespaced key
String userKey = RedisKeyConstants.createNamespacedKey("user", "profile", "123");
// Result: "radp:user:profile:123"
```

### Creating a Module-Specific Key Provider

For more complex applications, create a module-specific implementation of `RedisKeyProvider`:

```java
public enum MyModuleRedisKeys implements RedisKeyProvider {
    INSTANCE;

    private static final String PREFIX = "radp:mymodule";

    // Module-specific key constants
    public static final String USER_PROFILE = createKey("user:profile");
    public static final String SESSION_DATA = createKey("session");

    @Override
    public String getPrefix() {
        return PREFIX;
    }

    @Override
    public String buildKey(String... parts) {
        return RedisKeyConstants.buildRedisKeyWithPrefix(getPrefix(), parts);
    }

    // Static utility method for convenience
    public static String createKey(String... parts) {
        return INSTANCE.buildKey(parts);
    }

    // Additional helper methods specific to this module
    public static String buildUserKey(String userId) {
        return createKey("user", userId);
    }
}
```

### Using a Module-Specific Key Provider

```java
// Using predefined constants
String profileKey = MyModuleRedisKeys.USER_PROFILE;
// Result: "radp:mymodule:user:profile"

// Using helper methods
String userKey = MyModuleRedisKeys.buildUserKey("123");
// Result: "radp:mymodule:user:123"
```

## Best Practices

1. **Use the Provider Pattern**: Create a module-specific implementation of `RedisKeyProvider` for each module that uses
   Redis
2. **Define Constants**: Define commonly used keys as constants in your provider class
3. **Add Helper Methods**: Create helper methods for building complex keys
4. **Use Enum Singleton**: Implement the provider as an enum singleton to ensure thread safety
5. **Follow Naming Conventions**: Use consistent naming conventions for keys (e.g.,
   `application:module:entity:identifier`)
6. **Document Keys**: Add clear documentation for each key constant and helper method

## Example Implementation

See `TestRedisKeys` in the test module for a complete example implementation.

## Benefits

- **Centralized Management**: All Redis keys are defined in a centralized location
- **Standardized Format**: Keys follow a consistent format across the application
- **Type Safety**: Using enums and constants provides better type safety than string literals
- **Extensibility**: Each module can define its own keys while leveraging common utilities
- **Discoverability**: IDE auto-completion makes it easy to find available keys