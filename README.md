# TemplatePlugin

[![Jitpack latest version](https://jitpack.io/v/fr.xpdustry/TemplatePlugin.svg)](https://jitpack.io/#fr.xpdustry/TemplatePlugin)
[![Build status](https://github.com/Xpdustry/TemplatePlugin/actions/workflows/build.yml/badge.svg?branch=master&event=push)](https://github.com/Xpdustry/TemplatePlugin/actions/workflows/build.yml)
[![Mindustry 6.0 | 7.0 ](https://img.shields.io/badge/Mindustry-6.0%20%7C%207.0-ffd37f)](https://github.com/Anuken/Mindustry/releases)

## Description

Get your Mindustry plugin started with this awesome template repository, it features :

- [Jitpack](https://jitpack.io/) support.

- GitHub actions for easier releases.

- [Toxopid](https://plugins.gradle.org/plugin/fr.xpdustry.toxopid) gradle plugin for faster development.

- [Indra](https://plugins.gradle.org/plugin/net.kyori.indra) gradle plugin for easier java development.

## Building

- `./gradlew jar` for a simple jar that contains only the plugin code.

- `./gradlew shadowJar` for a fatJar that contains the plugin and its dependencies (use this for your server).

## Testing

- `./gradlew runMindustryClient`: Run Mindustry in desktop with the plugin.

- `./gradlew runMindustryServer`: Run Mindustry in a server with the plugin.

## Running

This plugin is runs on Java 17 and is compatible with Mindustry V6 and V7.

## Nice tips

- When using this template, don't forget to change `plugin.json`, `gradle.properties` and `LiCENSE.md`.

- Don't forget to bump your dependencies with the `dependencyUpdates` task.

- How to make a release :
 
  - Create a draft release on GitHub to which you will add elements as you develop.

  - Once you are ready, remove the `-SNAPSHOT` from your plugin version, push it and publish your release.
    The workflow fill be triggered automatically. After that, put back the `-SNAPSHOT` in your plugin version.
