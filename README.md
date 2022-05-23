# TemplatePlugin

[![Xpdustry latest](https://repo.xpdustry.fr/api/badge/latest/snapshots/fr/xpdustry/template-plugin?color=00FFFF&name=TemplatePlugin&prefix=v)](https://github.com/Xpdustry/TemplatePlugin/releases)
[![Build status](https://github.com/Xpdustry/TemplatePlugin/actions/workflows/build.yml/badge.svg?branch=master&event=push)](https://github.com/Xpdustry/TemplatePlugin/actions/workflows/build.yml)
[![Mindustry 6.0 | 7.0 ](https://img.shields.io/badge/Mindustry-6.0%20%7C%207.0-ffd37f)](https://github.com/Anuken/Mindustry/releases)

## Description

**Xpdustry variation for publishing packages to our repo.**

Get your Mindustry plugin started with this awesome template repository, it features :

- [Jitpack](https://jitpack.io/) support.

- GitHub actions for easier testing (the plugin is built for each commit and pull request).

- [Toxopid](https://plugins.gradle.org/plugin/fr.xpdustry.toxopid) Gradle plugin for faster
  Mindustry development.

- [Indra](https://plugins.gradle.org/plugin/net.kyori.indra) Gradle plugin for easier Java
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

This plugin runs on Java 17 and is compatible with Mindustry V6 and V7.

## Nice tips

- When using this template, don't forget to :

    - Change `plugin.json` and `gradle.properties`.

    - Update `LICENSE.md` and `LICENSE_HEADER.md` with your name or replace them with your own license.

      > if you do replace the license, change also the license property in the `indra` configuration of the build
      script, more info in the
      [Indra wiki](https://github.com/KyoriPowered/indra/wiki/indra-publishing#indra-extension-properties-and-methods).

    - Reset `CHANGELOG.md`.

- The changelog can be automatically generated to a draft release by the release-drafter workflow using 
  the [conventional commit](https://www.conventionalcommits.org/en/v1.0.0/) specification.
  When publishing the release, the changelog will be automatically committed to the `CHANGELOG.md` file.

- If you want to expose some of your plugin dependencies, or you are using SQL drivers, you will have to shade all your
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

- If you are from Xpdustry, before doing anything, run this in your terminal to set the `xpdustry-master` branch as master :

  ```batch
  git fetch origin xpdustry-master
  git checkout xpdustry-master
  git branch -m master old-master
  git branch -m xpdustry-master master
  git branch -rD origin/master
  git push origin master -f
  git branch -D old-master
  git push origin --delete xpdustry-master
  ```
