# WorldGuard-AnnotatedFlags

![Build](https://github.com/dandan2611/wg-annotatedflags/actions/workflows/build.yml/badge.svg)

Easily register WorldGuard flags using annotations.

## Installation

1. Install latest version of [WorldGuard](https://dev.bukkit.org/projects/worldguard) on your server.
2. Compile the utility classes in your project using [Maven](https://maven.apache.org/) or [Gradle](https://gradle.org/).

### Example of importing the API with Gradle (Kotlin)

```kotlin
repositories {
    maven(url = "https://nexus.codinbox.fr/repository/maven-public/")
}

dependencies {
    implementation("fr.codinbox:wg-annotatedflags:1.0.0")
}
```

## Usage

### @Register annotation

In your project class, you can declare a flag by creating a static field and annotating it with `@RegisterTYPEFlag` where `TYPE` is the type of the flag.

Using the annotation, you can specify the name of the flag (when it will be registered to WorldGuard) and its (depending on the type) default value.

```java
public class MyFlags {

    @RegisterStringFlag(name = "my-flag")
    public static final StringFlag MY_FLAG;

}
```

### Scanning flags in a class

To scan a class for flags, you can use the `FlagScanner` class.

```java

public class MyPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        final var registry = WorldGuard.getInstance().getFlagRegistry();

        try {
            FlagScanner.registerFlags(MyFlags.class, registry);
        } catch (FlagConflictException | IllegalAccessException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Unable to register custom WorldGuard flags", e);
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

}
```

### Default supported flags

The following flags are supported by default:

| Flag type | Annotation               | Can have default value |
|-----------|--------------------------| --- |
| Integer   | `@RegisterIntegerFlag`   | ❌    |
| Location  | `@RegisterLocationFlag`  | ❌     |
| State     | `@RegisterStateFlag`     | ✅      |
| String    | `@RegisterStringFlag`    | ✅       |
| Timestamp | `@RegisterTimestampFlag` | ❌        |
| UUID      | `@RegisterUUIDFlag`      | ❌         |

### Registering custom flags or custom annotations

You can register custom flags or custom annotations by implementing the `FlagBuilder.FlagConstructor` interface.

```java

public class MyPlugin extends JavaPlugin {

    @Override
    public void onLoad() {
        // Prefer to register flags in the onLoad() method
        // This will ensure that the flags are registered before the FlagScanner is used, and that other
        // plugins can use the flags
        
        // Register a "IntegerFlag" constructor for the "RegisterIntegerFlag" annotation
        // This implementation is already provided by the library
        FlagBuilder.registerConstructor(RegisterIntegerFlag.class,
                (annotation, field) -> new com.sk89q.worldguard.protection.flags.IntegerFlag(
                        ((RegisterIntegerFlag) annotation).name()
                )
        );
    }

}

```