# Atlas

[![Gradle CI](https://img.shields.io/github/workflow/status/PrimordialMoros/Atlas/Build?style=flat-square)](https://github.com/PrimordialMoros/Atlas/actions)
[![License](https://img.shields.io/github/license/PrimordialMoros/Atlas?color=blue&style=flat-square)](LICENSE)
[![GitHub release](https://img.shields.io/github/v/release/PrimordialMoros/Atlas?style=flat-square)](https://github.com/PrimordialMoros/Atlas/releases)

Atlas contains [Storage](https://github.com/PrimordialMoros/Storage) and several other libraries and frameworks that I commonly use in my projects.
For a full list, look at the build script inside the `atlas-core` module.

## Building

This project requires Java 11 or newer and uses Gradle (which comes with a wrapper, so you don't need to install it).

Module `atlas-paper` can be used to build a dummy plugin jar for PaperMC servers that adds `atlas-core` to the classpath.

Open a terminal and run `./gradlew build`
