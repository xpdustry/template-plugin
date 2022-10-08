# TemplatePlugin

[![Jitpack latest version](https://jitpack.io/v/fr.xpdustry/TemplatePlugin.svg)](https://jitpack.io/#fr.xpdustry/TemplatePlugin)
[![Build status](https://github.com/Xpdustry/TemplatePlugin/actions/workflows/build.yml/badge.svg?branch=master&event=push)](https://github.com/Xpdustry/TemplatePlugin/actions/workflows/build.yml)
[![Mindustry 6.0 | 7.0 ](https://img.shields.io/badge/Mindustry-6.0%20%7C%207.0-ffd37f)](https://github.com/Anuken/Mindustry/releases)

## Description

Get your Mindustry plugin started with this awesome template repository, it features :

- [Jitpack](https://jitpack.io/) support for using the project as a library.

- GitHub actions for easier testing (the plugin is built for each commit and pull request).

- [Toxopid](https://plugins.gradle.org/plugin/fr.xpdustry.toxopid) Gradle plugin for faster Mindustry plugin
  development and testing.

- [Indra](https://plugins.gradle.org/plugin/net.kyori.indra) Gradle plugin for easier project development.

    - It also comes with `indra.licenser-spotless`, an indra extension enabling the usage of [spotless](https://github.com/diffplug/spotless), a powerful formatting plugin that applies your
      licence header automatically in your source files with the `./gradlew spotlessApply` task (and much more).

- Jar bundling and automatic shading (isolating your dependencies) with the
  [Shadow](https://imperceptiblethoughts.com/shadow/) gradle plugin.

    - The default shaded dependencies location is `(rootpackage).shadow` (example: `fr.xpdustry.template.shadow`).

    - The bundled jar is stripped from every unused classes.

- Very easy base configuration :

    - If you're not an advanced user, just editing the properties in `plugin.json` and `gradle.properties` is enough.
      For example :
  
        - Changing `version` in `plugin.json` will change the whole project version.

        - The project is compiled with the version of Mindustry provided by `minGameVersion` in `plugin.json`.

## Building

- `./gradlew jar` for a simple jar that contains only the plugin code.

- `./gradlew shadowJar` for a fatJar that contains the plugin and its dependencies (use this for
  your server).

## Testing

- `./gradlew runMindustryClient`: Run the plugin in a Mindustry client (desktop).

- `./gradlew runMindustryServer`: Run the plugin in a Mindustry server (headless).

## Running

This plugin compiles to and run on Java 17 and is compatible with Mindustry V6 and V7.

## Nice tips

- If you eventually need to change the project licence (`LICENSE.md` and `LICENSE_HEADER.md`), also change the licence
  function call in the `indra` configuration of the build script, more info in the
  [Indra wiki](https://github.com/KyoriPowered/indra/wiki/indra-publishing#indra-extension-properties-and-methods).

- Don't forget to reset `CHANGELOG.md`.

- The changelog can be automatically generated in a draft release by the `release-drafter` workflow using 
  the [conventional commit](https://www.conventionalcommits.org/en/v1.0.0/) specification.
  When publishing the release, the changelog will be automatically committed to the `CHANGELOG.md` file.

  > Be aware that `release-drafter` won't work without an initial release like `0.1.0` or something like that.

- If you want to expose some of your plugin dependencies, or you are using SQL drivers, you will have to shade your
  dependencies manually by replacing :

  ```gradle
  val relocate = tasks.create<ConfigureShadowRelocation>("relocateShadowJar") {
      target = tasks.shadowJar.get()
      prefix = project.property("props.root-package").toString() + ".shadow"
  }

  tasks.shadowJar {
      dependsOn(relocate)
      minimize()
  }
  ```

  With :

  ```gradle
  tasks.shadowJar {
      val shadowPackage = project.property("props.root-package").toString() + ".shadow"
      // Put the internal dependencies here
      relocate("com.example.artifact1", "$shadowPackage.artifact1")
      relocate("com.example.artifact2", "$shadowPackage.artifact2")
      minimize {
          // Put the exposed dependencies and sql drivers here
          exclude(dependency("com.example:artifact3:.*"))
          exclude(dependency("com.example:some-sql-driver:.*"))
      }
  }
  ```
