# TemplatePlugin

[![Jitpack latest version](https://jitpack.io/v/fr.xpdustry/TemplatePlugin.svg)](https://jitpack.io/#fr.xpdustry/TemplatePlugin)
[![Build status](https://github.com/Xpdustry/TemplatePlugin/actions/workflows/build.yml/badge.svg?branch=master&event=push)](https://github.com/Xpdustry/TemplatePlugin/actions/workflows/build.yml)
[![Mindustry 6.0 | 7.0 ](https://img.shields.io/badge/Mindustry-6.0%20%7C%207.0-ffd37f)](https://github.com/Anuken/Mindustry/releases)

## Description

Get your Mindustry plugin started with this awesome template repository, it features :

- [Jitpack](https://jitpack.io/) support.

- GitHub actions for easier testing (the plugin is built for each commit and pull request).

- [Toxopid](https://plugins.gradle.org/plugin/fr.xpdustry.toxopid) gradle plugin for faster
  Mindustry development.

- [Indra](https://plugins.gradle.org/plugin/net.kyori.indra) gradle plugin for easier Java
  development.

    - This template also comes with `indra.license-header` to apply the project license in every source file.

- Jar bundling and automatic shading with the [Shadow](https://imperceptiblethoughts.com/shadow/) gradle plugin.

    - The default shaded dependencies location is `(rootpackage).shadow` (example: `fr.xpdustry.template.shadow`).

    - The bundled jar is stripped from every unused classes.

## Building

- `./gradlew jar` for a simple jar that contains only the plugin code.

- `./gradlew shadowJar` for a fatJar that contains the plugin and its dependencies (use this for
  your server).

## Testing

- `./gradlew runMindustryClient`: Run Mindustry in desktop with the plugin.

- `./gradlew runMindustryServer`: Run Mindustry in a server with the plugin.

## Running

This plugin is runs on Java 17 and is compatible with Mindustry V6 and V7.

## Nice tips

- When using this template, don't forget to :

    - Change `plugin.json` and `gradle.properties`.

    - Update `LICENSE.md` and `LICENSE_HEADER.md` with your name and/or replace them with your own license.

    - Reset `CHANGELOG.md`.

- Don't forget to bump your dependencies with the `dependencyUpdates` task.

- Add the changes of your plugin in `CHANGELOG.md` as you develop.

- How to make a release in 3 steps :

    1. Remove the `-SNAPSHOT` from your plugin version in `plugin.json`.

    2. Create a release on GitHub (don't forget to add the changelog). The workflow will be
       triggered automatically.

    3. Put back the `-SNAPSHOT` in your plugin version in `plugin.json`.

- If you want to expose some of your plugin dependencies, or you are using sql drivers, you will have to shade all your
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
