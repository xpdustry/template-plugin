# TemplatePlugin

[![Jitpack latest version](https://jitpack.io/v/fr.xpdustry/TemplatePlugin.svg)](https://jitpack.io/#fr.xpdustry/TemplatePlugin)
[![Build status](https://github.com/Xpdustry/TemplatePlugin/actions/workflows/build.yml/badge.svg?branch=master&event=push)](https://github.com/Xpdustry/TemplatePlugin/actions/workflows/build.yml)
[![Mindustry 6.0 | 7.0 ](https://img.shields.io/badge/Mindustry-6.0%20%7C%207.0-ffd37f)](https://github.com/Anuken/Mindustry/releases)

## Description

Get your Mindustry plugin started with this awesome template repository, it features :

- [Jitpack](https://jitpack.io/) support.

- GitHub actions for easier testing (the plugin is built for each commit and pull request).

- [Toxopid](https://plugins.gradle.org/plugin/fr.xpdustry.toxopid) gradle plugin for faster Mindustry development.

- [Indra](https://plugins.gradle.org/plugin/net.kyori.indra) gradle plugin for easier Java development.

## Building

- `./gradlew jar` for a simple jar that contains only the plugin code.

- `./gradlew shadowJar` for a fatJar that contains the plugin and its dependencies (use this for your server).

## Testing

- `./gradlew runMindustryClient`: Run Mindustry in desktop with the plugin.

- `./gradlew runMindustryServer`: Run Mindustry in a server with the plugin.

## Running

This plugin is runs on Java 17 and is compatible with Mindustry V6 and V7.

## Nice tips

- When using this template, don't forget to :

  - Change `plugin.json` and `gradle.properties`.

  - Update `LiCENSE.md` with your name and/or replace it with your own license.

  - Reset `CHANGELOG.md`.

- Don't forget to bump your dependencies with the `dependencyUpdates` task.

- Add the changes of your plugin in `CHANGELOG.md` as you develop.

- How to make a release in 3 steps :

  1. Remove the `-SNAPSHOT` from your plugin version in `plugin.json`.

  2. Create a release on GitHub (don't forget to add the changelog). The workflow will be triggered automatically.

  3. Put back the `-SNAPSHOT` in your plugin version in `plugin.json`.
