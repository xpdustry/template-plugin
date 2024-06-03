# template-plugin

[![Build status](https://github.com/xpdustry/template-plugin/actions/workflows/build.yml/badge.svg?branch=master&event=push)](https://github.com/xpdustry/template-plugin/actions/workflows/build.yml)
[![Mindustry 7.0 ](https://img.shields.io/badge/Mindustry-7.0-ffd37f)](https://github.com/Anuken/Mindustry/releases)

Get your Mindustry plugin started with this awesome template repository, it features:

- GitHub actions for easier testing (the plugin is built for each commit and pull request).

- [Toxopid](https://plugins.gradle.org/plugin/com.xpdustry.toxopid) Gradle plugin for faster Mindustry plugin
  development and testing.

- [Indra](https://plugins.gradle.org/plugin/net.kyori.indra) Gradle plugin for easier java development.

- Bundling and automatic relocation (isolating your dependencies to avoid class loading issues) with the
  [Shadow](https://imperceptiblethoughts.com/shadow/) gradle plugin.

  - Unused classes are removed from the final jar.

- A `CHANGELOG.md` file that will be updated automatically when you create a release on GitHub.

## How to use

1. Update the `build.gradle.kts`, `settings.gradle.kts` and `plugin.json` files with your plugin data.

2. Clear `CHANGELOG.md` and update `LICENSE.md` with your name (or completely changing the license if needed)

3. Start **K O D I N G**.

4. When ready for a release, push your last changes with the release version. Then create a release on GitHub. Once published, the plugin jar will be built and added to the release and the `CHANGELOG.md` file will be updated with the release notes of the GitHub release.

## Installation

This plugin requires :

- Java 17 or above.

- Mindustry v146 or above.

## Building

- `./gradlew shadowJar` to compile the plugin into a usable jar (will be located
  at `builds/libs/(plugin-name).jar`).

- `./gradlew runMindustryServer` to run the plugin in a local Mindustry server.

- `./gradlew runMindustryDesktop` to start a local Mindustry client that will let you test the plugin.

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
