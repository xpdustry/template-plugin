# TemplatePlugin

[![Xpdustry latest](https://repo.xpdustry.fr/api/badge/latest/snapshots/fr/xpdustry/template-plugin?color=00FFFF&name=TemplatePlugin&prefix=v)](https://github.com/Xpdustry/TemplatePlugin/releases)
[![Build status](https://github.com/Xpdustry/TemplatePlugin/actions/workflows/build.yml/badge.svg?branch=master&event=push)](https://github.com/Xpdustry/TemplatePlugin/actions/workflows/build.yml)
[![Mindustry 6.0 | 7.0 ](https://img.shields.io/badge/Mindustry-6.0%20%7C%207.0-ffd37f)](https://github.com/Anuken/Mindustry/releases)

## Description

**Xpdustry variation for publishing packages to our repo.**

Get your Mindustry plugin started with this awesome template repository, it features :

- [Jitpack](https://jitpack.io/) support.

- GitHub actions for easier testing (the plugin is built for each commit and pull request).

- [Toxopid](https://plugins.gradle.org/plugin/fr.xpdustry.toxopid) gradle plugin for faster
  Mindustry development.

- [Indra](https://plugins.gradle.org/plugin/net.kyori.indra) gradle plugin for easier Java
  development.

  - This template also comes with `indra.license-header` to apply the project license in every source file.

- Jar bundling and automatic shading with the [Shadow](https://imperceptiblethoughts.com/shadow/) gradle plugin.
  The default shaded dependencies location is `(rootpackage).shadow` (example: `fr.xpdustry.template.shadow`).

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

- If you want to expose some of your plugin dependencies, or you are using sql drivers, exclude them in the `shadowJar`
  task with :

  ```gradle
  tasks.shadowJar {
      minimize {
          exclude(dependency("the-group:the-artifact:.*"))
          // ...
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
