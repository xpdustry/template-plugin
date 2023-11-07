# template-plugin

[![Xpdustry latest](https://maven.xpdustry.com/api/badge/latest/releases/com/xpdustry/template?color=00FFFF&name=template-plugin&prefix=v)](https://github.com/xpdustry/template-plugin/releases)
[![Build status](https://github.com/xpdustry/template-plugin/actions/workflows/build.yml/badge.svg?branch=master&event=push)](https://github.com/xpdustry/template-plugin/actions/workflows/build.yml)
[![Mindustry 7.0 ](https://img.shields.io/badge/Mindustry-7.0-ffd37f)](https://github.com/Anuken/Mindustry/releases)

**Xpdustry variation for publishing packages to our maven repo.**

Get your Mindustry plugin started with this awesome template repository, it features :

- GitHub actions for easier testing (the plugin is built for each commit and pull request).

- [Toxopid](https://plugins.gradle.org/plugin/fr.xpdustry.toxopid) Gradle plugin for faster Mindustry plugin
  development and testing.

- [Indra](https://plugins.gradle.org/plugin/net.kyori.indra) Gradle plugin for easier java development.

- [Spotless](https://plugins.gradle.org/plugin/com.diffplug.spotless) Gradle plugin for code formatting.

  - It is set up to use palantir java format, a very good code formatter balanced between google and intellij codestyle.

  - Indra also comes with `indra.licenser-spotless`, a simple tool that will add your licence header automatically in your source files.

- [Ben Manes' Gradle Versions Plugin](https://github.com/ben-manes/gradle-versions-plugin) for keeping your
  dependencies up to date.

- Bundling and automatic relocation (isolating your dependencies to avoid class loading issues) with the
  [Shadow](https://imperceptiblethoughts.com/shadow/) gradle plugin.

  - Unused classes are removed from the final jar.

- The build script contains a lot of comments to help you understand what is going on.

- A `CHANGELOG.md` file that will be updated automatically when you create a release on GitHub.

## How to use

1. Update the `build.gradle.kts` file with your plugin data.

2. Clear `CHANGELOG.md` and update `LICENSE.md` with your name.

3. Start **K O D I N G**.

4. When ready for a release, push your last changes with the release version. Then create a release on GitHub. Once published, the plugin jar will be built and added to the release and the `CHANGELOG.md` file will be updated with the release notes of the GitHub release.

## Installation

This plugin requires :

- Java 17 or above.

- Mindustry v146 or above.

## Building

- `./gradlew shadowJar` to compile the plugin into a usable jar (will be located
  at `builds/libs/(plugin-name).jar`).

- `./gradlew jar` for a plain jar that contains only the plugin code.

- `./gradlew runMindustryServer` to run the plugin in a local Mindustry server.

- `./gradlew runMindustryClient` to start a local Mindustry client that will let you test the plugin.

- `./gradlew spotlessApply` to apply the code formatting and the licence header.

- `./gradlew dependencyUpdates` to check for dependency updates.

## Note

If you are from Xpdustry, before doing anything, run this in your terminal to set the `xpdustry-master` branch as master :

```bash
git fetch origin xpdustry-master
git checkout xpdustry-master
git branch -m master old-master
git branch -m xpdustry-master master
git branch -rD origin/master
git push origin master -f
git branch -D old-master
git push origin --delete xpdustry-master
```
