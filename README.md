# Singularity

A Scala monorepo project built with sbt, demonstrating modular architecture with multiple subprojects.

## Project Structure

This monorepo contains the following subprojects:

- **core**: Core functionality and base classes
- **utils**: Utility functions that depend on core
- **examples**: Example applications demonstrating the use of other modules

## Prerequisites

- Java 11 or higher
- sbt 1.9.7 or higher

## Getting Started

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd singularity
   ```

2. Build the project:
   ```bash
   sbt compile
   ```

3. Run tests:
   ```bash
   sbt test
   ```

4. Run examples:
   ```bash
   sbt "examples/run"
   ```

## Available Commands

- `sbt compile` - Compile all subprojects
- `sbt test` - Run all tests
- `sbt "core/test"` - Run tests for core module only
- `sbt "utils/test"` - Run tests for utils module only
- `sbt "examples/run"` - Run the examples application
- `sbt clean` - Clean build artifacts
- `sbt reload` - Reload sbt configuration

## Project Dependencies

- **core**: No dependencies (base module)
- **utils**: Depends on core
- **examples**: Depends on both core and utils

## Adding New Subprojects

To add a new subproject:

1. Create a new directory for your subproject
2. Add the subproject definition to `build.sbt`
3. Update the root project's `aggregate` setting to include the new subproject
4. Create the standard `src/main/scala` and `src/test/scala` directory structure

Example subproject definition in `build.sbt`:
```scala
lazy val newModule = (project in file("new-module"))
  .settings(
    name := "singularity-new-module",
    libraryDependencies ++= Seq(
      "org.scalameta" %% "munit" % "0.7.29" % Test
    )
  )
  .dependsOn(core) // if it depends on core
```

## Technology Stack

- **Language**: Scala 3.3.1
- **Build Tool**: sbt 1.9.7
- **Testing**: MUnit
- **Organization**: com.singularity

## Contributing

1. Create a feature branch
2. Make your changes
3. Add tests for new functionality
4. Run `sbt test` to ensure all tests pass
5. Submit a pull request
