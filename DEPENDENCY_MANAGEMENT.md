# Dependency Management System

This document explains the two-tier dependency management system implemented in the Singularity monorepo.

## Architecture Overview

The dependency management system consists of two levels:

1. **Global Level**: Centralized dependency and version management
2. **Subproject Level**: Project-specific overrides and additions

## File Structure

```
project/
├── Dependencies.scala    # Global dependency definitions
├── Projects.scala        # Project configurations with overrides
├── build.properties     # sbt version
└── plugins.sbt         # sbt plugins

build.sbt               # Main build file (imports and delegates)
```

## Global Dependency Management (`project/Dependencies.scala`)

### Version Definitions
All versions are centralized in the `Versions` object:

```scala
object Versions {
  val scala = "3.3.1"
  val spark = "3.5.0"
  val munit = "0.7.29"
  // ... more versions
}
```

### Dependency Groups
Dependencies are organized into logical groups:

```scala
object Groups {
  val logging = Seq(Core.scalaLogging, Core.logback, Core.slf4j)
  val testing = Seq(Core.scalaTest, Core.munit)
  val sparkCore = Seq(Spark.core, Spark.sql)
  val sparkFull = Seq(Spark.core, Spark.sql, Spark.streaming, Spark.mllib, Spark.graphx)
  // ... more groups
}
```

### Benefits
- **Consistency**: All projects use the same versions by default
- **Maintainability**: Update versions in one place
- **Reusability**: Common dependency groups can be reused
- **Organization**: Dependencies are logically grouped

## Subproject-Level Overrides (`project/Projects.scala`)

### Common Settings
All projects inherit common settings:

```scala
val commonSettings = Seq(
  scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked", "-Xfatal-warnings"),
  Test / parallelExecution := false
)
```

### Dependency Overrides
Projects can override global versions using `dependencyOverrides`:

```scala
lazy val sparkCore = (project in file("spark-core"))
  .settings(
    // Override Spark version for this specific project
    dependencyOverrides ++= Seq(
      "org.apache.spark" %% "spark-core" % "3.4.2", // Override global 3.5.0
      "org.apache.spark" %% "spark-sql" % "3.4.2"
    )
  )
```

### Project-Specific Dependencies
Projects can add dependencies not defined globally:

```scala
lazy val analytics = (project in file("analytics"))
  .settings(
    libraryDependencies ++= Seq(
      "org.apache.commons" % "commons-math3" % "3.6.1",
      "org.apache.commons" % "commons-lang3" % "3.12.0"
    )
  )
```

## Usage Examples

### Adding a New Subproject

1. **Add to `Projects.scala`**:
```scala
lazy val newModule = (project in file("new-module"))
  .settings(
    commonSettings,
    name := "singularity-new-module",
    libraryDependencies ++= Groups.testing ++ Groups.logging,
    // Optional: Override specific dependencies
    dependencyOverrides ++= Seq(
      "some.library" %% "some-artifact" % "custom-version"
    )
  )
  .dependsOn(core)
```

2. **Add to `build.sbt`**:
```scala
lazy val newModule = Projects.newModule
```

3. **Update root project aggregation**:
```scala
lazy val root = (project in file("."))
  .aggregate(core, utils, examples, sparkCore, analytics, newModule)
```

### Overriding Global Versions

To override a global version for a specific project:

```scala
lazy val specialProject = (project in file("special"))
  .settings(
    dependencyOverrides ++= Seq(
      "org.apache.spark" %% "spark-core" % "3.3.4", // Override global 3.5.0
      "com.typesafe" % "config" % "1.3.4"           // Override global 1.4.3
    )
  )
```

### Adding New Global Dependencies

1. **Add version to `Versions`**:
```scala
object Versions {
  val newLibrary = "2.1.0"
}
```

2. **Add dependency to appropriate group**:
```scala
object NewLibrary {
  val core = "com.example" %% "new-library" % Versions.newLibrary
}
```

3. **Add to groups if commonly used**:
```scala
object Groups {
  val newLibraryGroup = Seq(NewLibrary.core)
}
```

## Best Practices

### Global Level
- Keep all versions in `Versions` object
- Group related dependencies together
- Use semantic versioning
- Document breaking changes

### Subproject Level
- Use `dependencyOverrides` sparingly
- Document why overrides are necessary
- Consider if override should be global
- Test thoroughly with overrides

### Dependency Groups
- Create logical groupings
- Reuse groups across projects
- Keep groups focused and cohesive
- Document group purposes

## Commands

### View Dependency Tree
```bash
sbt dependencyTree
```

### Check for Updates
```bash
sbt dependencyUpdates
```

### Resolve Dependencies
```bash
sbt update
```

### Clean and Rebuild
```bash
sbt clean compile
```

## Troubleshooting

### Version Conflicts
- Check `dependencyTree` for conflicts
- Use `dependencyOverrides` to resolve
- Consider updating global versions

### Missing Dependencies
- Add to appropriate group in `Dependencies.scala`
- Include in project's `libraryDependencies`
- Update `Versions` if needed

### Build Issues
- Run `sbt clean` to clear cache
- Check for conflicting overrides
- Verify all projects compile individually
