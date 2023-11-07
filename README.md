# TemplatePlugin

[![Build status](https://github.com/Xpdustry/TemplatePlugin/actions/workflows/build.yml/badge.svg?branch=master&event=push)](https://github.com/Xpdustry/TemplatePlugin/actions/workflows/build.yml)
[![Mindustry 7.0 ](https://img.shields.io/badge/Mindustry-7.0-ffd37f)](https://github.com/Anuken/Mindustry/releases)

Get your Mindustry plugin started with this awesome template repository, it features :

- GitHub actions for easier testing (the plugin is built for each commit and pull request).

- [Toxopid](https://plugins.gradle.org/plugin/fr.xpdustry.toxopid) Gradle plugin for faster Mindustry plugin
  development and testing.

- [Indra](https://plugins.gradle.org/plugin/net.kyori.indra) Gradle plugin for easier java development.

  - It also comes with `indra.licenser-spotless`, a powerful formatting tool that applies your
    licence header automatically in your source files with the `./gradlew spotlessApply` task (and much more).

- Bundling and automatic relocation (isolating your dependencies to avoid class loading issues) with the
  [Shadow](https://imperceptiblethoughts.com/shadow/) gradle plugin.

  - The default relocation is `(plugin-root-package).shadow` (example with gson: `fr.xpdustry.template.shadow.gson`).

  - Unused classes are removed from the final jar.

- Very easy configuration :

## How to use

1. Update the `build.gradle.kts` file with your plugin data.

2. Clear `CHANGELOG.md`.

3. Start **K O D I N G**.

4. When ready for a release, push your last changes with the release version. Then create a release on GitHub. Once published, the plugin jar will be built and added to the release and the `CHANGELOG.md` file will be updated with the release notes of the GitHub release.

## Installation

This plugin requires :

- Java 17 or above.

- Mindustry v146 or above.

## Building

- `./gradlew shadowJar` to compile the plugin into a usable jar (will be located
  at `builds/libs/(plugin-display-name).jar`).

- `./gradlew jar` for a plain jar that contains only the plugin code.

- `./gradlew runMindustryServer` to run the plugin in a local Mindustry server.

## Notes

- If you eventually need to change the project licence (`LICENSE.md` and `LICENSE_HEADER.md`), also change the licence
  function call in the `indra` configuration of the build script, more info in the
  [Indra wiki](https://github.com/KyoriPowered/indra/wiki/indra-publishing#indra-extension-properties-and-methods).
