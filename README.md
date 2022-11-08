# TemplatePlugin

[![Xpdustry latest](https://repo.xpdustry.fr/api/badge/latest/snapshots/fr/xpdustry/template-plugin?color=00FFFF&name=TemplatePlugin&prefix=v)](https://github.com/Xpdustry/TemplatePlugin/releases)
[![Build status](https://github.com/Xpdustry/TemplatePlugin/actions/workflows/build.yml/badge.svg?branch=master&event=push)](https://github.com/Xpdustry/TemplatePlugin/actions/workflows/build.yml)
[![Mindustry 6.0 | 7.0 ](https://img.shields.io/badge/Mindustry-6.0%20%7C%207.0-ffd37f)](https://github.com/Anuken/Mindustry/releases)

## Description

**Xpdustry variation for publishing packages to our repo.**

Get your Mindustry plugin started with this awesome template repository, it features :

- [Jitpack](https://jitpack.io/) support for using the project as a library.

- GitHub actions for easier testing (the plugin is built for each commit and pull request).

- [Toxopid](https://plugins.gradle.org/plugin/fr.xpdustry.toxopid) Gradle plugin for faster Mindustry plugin
  development and testing.

- [Indra](https://plugins.gradle.org/plugin/net.kyori.indra) Gradle plugin for easier java project development.

  - It also comes with `indra.licenser-spotless`, a powerful formatting tool that applies your
    licence header automatically in your source files with the `./gradlew spotlessApply` task (and much more).

- Bundling and automatic shading (isolating your dependencies to avoid class loading issues) with the
  [Shadow](https://imperceptiblethoughts.com/shadow/) gradle plugin.

  - The default shaded dependencies location is `(rootpackage).shadow` (example: `fr.xpdustry.template.shadow`).

  - The bundled jar is stripped from every unused classes.

- Very easy configuration :

  - If you're not an advanced user, just editing the properties in `plugin.json` and `gradle.properties` is enough.
    For example :

    - Changing `version` in `plugin.json` will change the whole project version.

    - The project is compiled with the version of Mindustry provided by `minGameVersion` in `plugin.json`.

## How to use

1. Update `plugin.json` and  `gradle.properties`.

2. Reset `CHANGELOG.md`.

3. This is the part you start **K O D I N G**.

4. When ready for release, set the release version in your `plugin.json`, push the change and create a release on 
   GitHub. Once published, the plugin jar will be added to the release and the `CHANGELOG.md` file will be updated
   with the release notes.

## Building

- `./gradlew jar` for a simple jar that contains only the plugin code.

- `./gradlew shadowJar` for a fat jar that contains the plugin and its dependencies (use this for
  your server, it's the jar ending with `-all` in `builds/libs`).

## Testing

- `./gradlew runMindustryClient`: Run the plugin in a Mindustry client (desktop).

- `./gradlew runMindustryServer`: Run the plugin in a Mindustry server (headless).

## Running

This plugin compiles to and run on Java 17 and is compatible with Mindustry V6 and V7.

## Nice tips

- If you eventually need to change the project licence (`LICENSE.md` and `LICENSE_HEADER.md`), also change the licence
  function call in the `indra` configuration of the build script, more info in the
  [Indra wiki](https://github.com/KyoriPowered/indra/wiki/indra-publishing#indra-extension-properties-and-methods).

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
